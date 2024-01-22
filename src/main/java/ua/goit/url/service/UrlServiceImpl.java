package ua.goit.url.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ua.goit.config.CachingConfig;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.exceptions.AlreadyExistUrlException;
import ua.goit.url.service.exceptions.NotAccessibleException;
import ua.goit.user.UserEntity;
import ua.goit.user.service.UserService;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.goit.url.UrlEntity.VALID_DAYS;


@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    private final UrlMapper urlMapper;
    private final UrlRepository urlRepository;
    private final ShortLinkGenerator shortLinkGenerator;
    private final UserService userService;

    @Override
    public List<UrlDto> listAll() {
        return urlMapper.toUrlDtos(urlRepository.findAll());
    }

    @Override
    @Transactional
    @Cacheable("urls")
    public UrlDto createUrl(String username, CreateUrlRequest url) {
        String originalUrl = getFullUrl(url.getUrl());

        if (!isUrlAccessible(originalUrl)) {
            throw new NotAccessibleException(originalUrl);
        }

        String shortUrl;
        do {
            shortUrl = shortLinkGenerator.generateShortLink();
        } while (!isLinkUnique(shortUrl));

        UrlDto urlDto = new UrlDto();
        urlDto.setShortUrl(shortLinkGenerator.generateShortLink());
        urlDto.setUrl(originalUrl);
        urlDto.setDescription(url.getDescription());
        urlDto.setUsername(username);
        urlDto.setVisitCount(0);
        return urlMapper.toUrlDto(urlRepository.save(urlMapper.toUrlEntity(urlDto)));

    }

    @Override
    public void deleteById(String username, Long id) {
        if (!urlRepository.existsById(id)) {
            throw new IllegalArgumentException("Url with id " + id + " not found");
        } else {
            Optional<UrlEntity> optionalUrl = urlRepository.findById(id);
            if (optionalUrl.isPresent()) {
                UrlEntity urlToDelete = optionalUrl.get();
                if (!urlToDelete.getUser().getUsername().equals(username)) {
                    throw new AccessDeniedException("Access forbidden");
                }

                UserEntity user = urlToDelete.getUser();
                user.getUrls().remove(urlToDelete);
            }
            urlRepository.deleteById(id);
        }
    }

    @Override
    public UrlDto update(String username, Long id, UpdateUrlRequest request) {
        UrlDto dto = getById(id);
        if (Objects.isNull(dto)) {
            throw new NoSuchElementException("Not found url with id: " + id);
        }
        if (!isLinkUnique(request.getShortUrl())) {
            Long idExistingLink = urlRepository.findByShortUrl(request.getShortUrl())
                    .orElseThrow()
                    .getId();
            if(!Objects.equals(idExistingLink, id)) {
                throw new AlreadyExistUrlException(request.getShortUrl());
            }
        }
        if (request.getShortUrl().isBlank()) {
            throw new IllegalArgumentException("Short link can't be empty");
        }
        if (!isUrlAccessible(getFullUrl(request.getUrl()))) {
            throw new NotAccessibleException(request.getUrl());
        }
        if (!dto.getUsername().equals(username)) {
            throw new AccessDeniedException("Access forbidden");
        }
        dto.setUrl(getFullUrl(request.getUrl()));
        dto.setShortUrl(request.getShortUrl());
        dto.setDescription(request.getDescription());
        UrlEntity urlEntity = urlMapper.toUrlEntity(dto);

        return urlMapper.toUrlDto(urlRepository.save(urlEntity));
    }

    @Override
    @Cacheable( value = "urls", key = "#p0")
    public UrlDto getById(Long id) {
        System.out.println("get");
        return urlMapper.toUrlDto(urlRepository.getReferenceById(id));
    }

    @Override
    public List<UrlDto> getAllUrlUser(String username) {
        Long userId = userService.findByUsername(username).getId();

        return urlRepository.findByUserId(userId)
                .stream()
                .map(urlMapper::toUrlDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UrlDto> getActiveUrlUser(String username) {
        Long userId = userService.findByUsername(username).getId();
        LocalDate currentDate = LocalDate.now();
        return urlMapper.toUrlDtos(urlRepository.findActiveUrlsByUserId(userId, currentDate));
    }

    @Override
    public List<UrlDto> getInactiveUrlUser(String username) {
        Long userId = userService.findByUsername(username).getId();
        LocalDate currentDate = LocalDate.now();
        return urlMapper.toUrlDtos(urlRepository.findInactiveUrlsByUserId(userId, currentDate));
    }

    @Override
    public List<UrlDto> getActiveUrl() {
        LocalDate currentDate = LocalDate.now();
        return urlMapper.toUrlDtos(urlRepository.findActiveUrls(currentDate));
    }

    @Override
    public List<UrlDto> getInactiveUrl() {
        LocalDate currentDate = LocalDate.now();
        return urlMapper.toUrlDtos(urlRepository.findInactiveUrls(currentDate));
    }

    @Override
    public void prolongation(String username, Long id) {
        if (!urlRepository.existsById(id)) {
            throw new IllegalArgumentException("Url with id " + id + " not found");
        } else {
            Optional<UrlEntity> optionalUrl = urlRepository.findById(id);
            if (optionalUrl.isPresent()) {
                UrlEntity urlToProlong = optionalUrl.get();
                if (!urlToProlong.getUser().getUsername().equals(username)) {
                    throw new AccessDeniedException("Access forbidden");
                }

                LocalDate newExpDate = urlToProlong.getExpirationDate().plusDays(VALID_DAYS);
                urlToProlong.setExpirationDate(newExpDate);
                urlRepository.save(urlToProlong);
            }
        }
    }

    public boolean isLinkUnique(String link) {
        return listAll().stream().noneMatch(urlEntity -> urlEntity.getShortUrl().equals(link));
    }

    private String getFullUrl(String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "https://" + url;
        }
        return url;
    }

    private boolean isUrlAccessible(String originalUrl) {
        int responseCode;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(originalUrl).openConnection();
            connection.setRequestMethod("HEAD");
            responseCode = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException e) {
            responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
        }
        return responseCode >= 200 && responseCode < 300;
    }
}

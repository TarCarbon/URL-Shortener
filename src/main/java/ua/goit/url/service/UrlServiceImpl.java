package ua.goit.url.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.exceptions.AlreadyExistUrlException;
import ua.goit.user.UserEntity;
import ua.goit.user.service.UserService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;

import java.util.Collections;
import ua.goit.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.Objects;


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
    public UrlDto createUrl (CreateUrlRequest url) {
        String shortUrl;
        if (!isUrlAccessible(url.getUrl())) {
            throw new NotAccessibleException(url);
        } else {
            do{
                shortUrl = shortLinkGenerator.generateShortLink();
            } while (!isLinkUnique(shortUrl));
            UrlDto urlDto = urlMapper.toUrlDto(url);
            urlDto.setShortUrl(shortLinkGenerator.generateShortLink());
            urlDto.setVisitCount(0);
            urlDto.setUsername("dima"); //тимчасово
            return urlMapper.toUrlDto(urlRepository.save(urlMapper.toUrlEntity(urlDto)));
        }
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
    public void update(String username, Long id, UpdateUrlRequest request) {
        UrlDto dto = getById(id);
        if (Objects.isNull(dto)) {
            throw new NoSuchElementException("Not found url with id: " + id);
        }
        if (!isLinkUnique(request.getShortUrl())) {
            throw new AlreadyExistUrlException(request.getShortUrl());
        }
        if (!dto.getUsername().equals(username)) {
            throw new AccessDeniedException("Access forbidden");
        }
        dto.setUrl(request.getUrl());
        dto.setShortUrl(request.getShortUrl());
        dto.setDescription(request.getDescription());
        UrlEntity urlEntity = urlMapper.toUrlEntity(dto);

        urlRepository.save(urlEntity);
    }

    @Override
    public UrlDto getById(Long id) {
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
    public List<UrlDto> getActiveUrls(String username) {
        Long userId = userService.findByUsername(username).getId();
        LocalDateTime currentDateTime = LocalDateTime.now();
        return urlMapper.toUrlDtos(urlRepository.findActiveUrlsByUserId(userId, currentDateTime));
    }

    @Override
    public List<UrlDto> getInactiveUrls(String username) {
        Long userId = userService.findByUsername(username).getId();
        LocalDateTime currentDateTime = LocalDateTime.now();
        return urlMapper.toUrlDtos(urlRepository.findInactiveUrlsByUserId(userId, currentDateTime));
    }

    public boolean isLinkUnique(String link) {
        return listAll().stream().noneMatch(urlEntity -> urlEntity.getShortUrl().equals(link));
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

package ua.goit.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;

import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.exceptions.AlreadyExistUrlException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.goit.url.service.exceptions.NotAccessibleException;
import ua.goit.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService{
    private final UrlMapper urlMapper;
    private final UrlRepository urlRepository;
    private final ShortLinkGenerator shortLinkGenerator;

    @Override
    public List<UrlDto> listAll() {
        return urlMapper.toUrlDtos(urlRepository.findAll());
    }

    @Override
    @Transactional
    public UrlDto createUrl (CreateUrlRequest url) throws NotAccessibleException {
        if (!isUrlAccessible(url.getUrl())) {
            throw new NotAccessibleException(url);
        } else {
            UrlDto urlDto = urlMapper.toUrlDto(url);
            urlDto.setShortUrl(shortLinkGenerator.generateShortLink());
            urlDto.setVisitCount(0);
            urlDto.setUsername("principal.getName()"); //тимчасово
            return urlMapper.toUrlDto(urlRepository.save(urlMapper.toUrlEntity(urlDto)));
        }
    }

    @Override
    public void deleteById(Long id) {
        if (!urlRepository.existsById(id)) {
            throw new IllegalArgumentException("Url with id " + id + " not found");
        } else {
            Optional<UrlEntity> optionalUrl = urlRepository.findById(id);
            if (optionalUrl.isPresent()) {
                UrlEntity urlToDelete = optionalUrl.get();
                UserEntity user = urlToDelete.getUser();
                user.getUrls().remove(urlToDelete);
            }
            urlRepository.deleteById(id);
        }
    }

    @Override
    public void update(Long id, UpdateUrlRequest request) {
        UrlDto dto = getById(id);
        if(Objects.isNull(dto)){
            throw new NoSuchElementException("Not found url with id: " + id);
        }
        if (!isLinkUnique(request.getShortUrl())){
            throw new AlreadyExistUrlException(request.getShortUrl());
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
    public List<UrlDto> getAllUrlUser(Long id) {
        return   urlRepository.findByUserId(id)
                .stream()
                .map(urlMapper::toUrlDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<UrlDto> getActiveUrls(Long userId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return urlMapper.toUrlDtos(urlRepository.findActiveUrlsByUserId(userId, currentDateTime));
    }

    @Override
    public List<UrlDto> getInactiveUrls(Long userId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return urlMapper.toUrlDtos(urlRepository.findInactiveUrlsByUserId(userId, currentDateTime));
    }


    public boolean isLinkUnique(String link) {
        return listAll().stream().noneMatch(urlEntity -> urlEntity.getShortUrl().equals(link));
    }

    //before passing to the method, check and if necessary add to the originalUrl https://
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

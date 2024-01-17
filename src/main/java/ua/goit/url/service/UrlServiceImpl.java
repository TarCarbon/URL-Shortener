package ua.goit.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.goit.url.UrlEntity;
import ua.goit.url.repository.UrlRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {


    @Autowired
    private UrlMapper urlMapper;
    @Autowired
    private UrlRepository urlRepository;

    @Override
    public List<UrlDto> listAll() {
        return urlMapper.toUrlDtos(urlRepository.findAll());
    }

    @Override
    public UrlDto add(CreateUrlRequest url) {
        return null;
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
    public void update(Long id, UpdateUrlRequest url) {

    }

    @Override
    public UrlDto getById(Long id) {
        return null;
    }

    @Override
    public List<UrlDto> getActiveUrls() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return urlMapper.toUrlDtos(urlRepository.findAll().stream()
                        .filter(urlEntity -> urlEntity.getExpirationDate().isAfter(currentDateTime))
                        .collect(Collectors.toList()));
    }

    @Override
    public List<UrlDto> getInactiveUrls() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return urlMapper.toUrlDtos(urlRepository.findAll().stream()
                        .filter(urlEntity -> urlEntity.getExpirationDate().isBefore(currentDateTime))
                        .collect(Collectors.toList()));
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

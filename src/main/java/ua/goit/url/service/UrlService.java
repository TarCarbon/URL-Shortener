package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.exceptions.NotAccessibleException;

import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();

    UrlDto createUrl(CreateUrlRequest url) throws NotAccessibleException;

    void deleteById(Long id);

    void update(Long id, UpdateUrlRequest url);

    UrlDto getById(Long id);

    List<UrlDto> getAllUrlUser(Long id);

    List<UrlDto> getActiveUrls(Long id);

    List<UrlDto> getInactiveUrls(Long id);
}
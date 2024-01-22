package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;

import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();

    UrlDto createUrl(String username, CreateUrlRequest url);

    void deleteById(String username, Long id);

    UrlDto update(String username, Long id, UpdateUrlRequest url);

    UrlDto getById(Long id);

    List<UrlDto> getAllUrlUser(String username);

    List<UrlDto> getActiveUrlUser(String username);

    List<UrlDto> getInactiveUrlUser(String username);

    List<UrlDto> getActiveUrl();

    List<UrlDto> getInactiveUrl();

    void prolongation(String username, Long id);
}

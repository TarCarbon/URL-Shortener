package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;

import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();
    UrlDto add(UrlDto url);
    void deleteByShortUrl(String id);
    void update(UrlDto url);
    UrlDto getByShortUrl(String shortUrl);
}

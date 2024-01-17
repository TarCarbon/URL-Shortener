package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.EditUrlRequest;

import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();

    UrlDto createUrl(UrlDto urlDto);

    void deleteById(Long id);

    void edit(Long id, EditUrlRequest url);

    UrlDto getById(Long id);
}

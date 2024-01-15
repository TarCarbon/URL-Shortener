package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;

import java.math.BigDecimal;
import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();

    UrlDto add(CreateUrlRequest url);

    void deleteById(Long id);

    void update(Long id, UpdateUrlRequest url);

    UrlDto getById(Long id);
}

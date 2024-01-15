package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;

import java.math.BigDecimal;
import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();
    UrlDto add(UrlDto url);
    void deleteById(BigDecimal id);
    void update(UrlDto url);
    UrlDto getById(BigDecimal id);
}

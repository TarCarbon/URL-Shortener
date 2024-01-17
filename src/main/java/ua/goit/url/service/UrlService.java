package ua.goit.url.service;

import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.exceptions.AlreadyExistUrlException;

import java.util.List;

public interface UrlService {
    List<UrlDto> listAll();

    UrlDto add(CreateUrlRequest url);

    void deleteById(Long id);

    void update(Long id, UpdateUrlRequest url) throws AlreadyExistUrlException;

    UrlDto getById(Long id);
}

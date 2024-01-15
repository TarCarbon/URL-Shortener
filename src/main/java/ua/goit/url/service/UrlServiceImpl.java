package ua.goit.url.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ua.goit.url.repository.UrlRepository;


import java.util.List;


@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository repository;

    public List<String> getAllShortUrls() {
        return repository.allShortUrls();
    }

}

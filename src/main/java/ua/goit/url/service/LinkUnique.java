package ua.goit.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LinkUnique {

    private final UrlServiceImpl urlService;

    public boolean isLinkUnique(String link) {
        return urlService.getAllShortUrls().contains(link);
    }
}


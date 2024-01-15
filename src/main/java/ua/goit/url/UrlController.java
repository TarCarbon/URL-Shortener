package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;

import java.util.List;

@RestController
@RequestMapping("/V1/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    UrlMapper urlMapper;

    @GetMapping("/list")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }
}

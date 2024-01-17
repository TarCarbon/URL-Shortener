package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.UrlRequest;
import ua.goit.url.response.UrlResponse;
import ua.goit.url.service.ShortLinkGenerator;
import ua.goit.url.service.UrlServiceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UrlController {
    private static final int SHORT_URL_LENGTH = 8;
    private static final String URL_PREFIX = "https://";
    @Autowired
    private UrlServiceImpl urlService;
    @Autowired
    private UrlMapper urlMapper;
    @Autowired
    private ShortLinkGenerator shortLinkGenerator;

    @GetMapping("/url/all")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }

    @GetMapping("/url/all")
    public ResponseEntity<List<UrlResponse>> allLinks() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(urlMapper.toUrlResponses(urlService.listAll()));
    }

    @PostMapping("/url/create")
    public ResponseEntity<UrlResponse> createLink(Principal principal, @RequestBody UrlRequest request) {
        String shortUrl = URL_PREFIX + UUID.randomUUID().toString().substring(0, SHORT_URL_LENGTH);
//        String shortUrl = shortLinkGenerator.generateShortLink(); //цей генератор чомусь не видає значення
        UrlDto urlDto = urlMapper.toUrlDto(request);
//        urlDto.setUsername(principal.getName()); //тимчасово відключаю, поки напишемо секьюріті
        urlDto.setCreatedDate(LocalDateTime.now());
        urlDto.setExpirationDate(LocalDateTime.now().plusDays(30));
        urlDto.setVisitCount(0);
        urlDto.setShortUrl(shortUrl);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(urlMapper.toUrlResponse(urlService.createUrl(urlDto)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        urlService.deleteById(id);
    }
}

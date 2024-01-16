package ua.goit.url;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.response.UrlResponse;
import ua.goit.url.service.UrlService;

import java.util.List;

@RestController
@RequestMapping("/V1/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlMapper urlMapper;

    @GetMapping("/url/all")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }

    @PostMapping("/url/create")
    public ResponseEntity<UrlResponse> createLink(
            @CookieValue(value = "username") String username,
            @Valid @NotNull @RequestBody CreateUrlRequest request) {
        UrlDto urlDto = urlMapper.toUrlDto(request);
        urlDto.setUsername(username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(urlMapper.toUrlResponse(urlService.createUrl(urlDto)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        urlService.deleteById(id);
    }
}

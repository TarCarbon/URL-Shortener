package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.response.UrlResponse;
import ua.goit.url.service.UrlService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/V1/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @GetMapping("/list")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }

    @PostMapping("/create")
    public UrlDto createUrl(@RequestBody CreateUrlRequest request) {
        return urlService.add(request);
    }

    @PutMapping("/{id}}")
    public void updateUrl(@PathVariable("id") Long id,
                          @RequestBody UpdateUrlRequest request) {
        urlService.update(id, request);
    }

    @DeleteMapping("/delete/{id}}")
        public void deleteUrlById(@PathVariable("id") Long id) {
        urlService.deleteById(id);
    }
}

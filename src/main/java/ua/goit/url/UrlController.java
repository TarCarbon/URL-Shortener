package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;
import ua.goit.url.service.exceptions.AlreadyExistUrlException;

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

    @PostMapping("/{id}/edit")
    public void updateUrl(@PathVariable("id") Long id,
                          @RequestBody UpdateUrlRequest request) throws AlreadyExistUrlException {
        urlService.update(id, request);
    }
}

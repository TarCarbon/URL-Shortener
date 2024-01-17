package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;
import ua.goit.url.service.exceptions.NotAccessibleException;

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
    public UrlDto createLink(@RequestBody CreateUrlRequest request) throws NotAccessibleException {
        UrlDto response = urlService.createUrl(request);
        ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        return response;
    }

    @GetMapping("/list/user/{id}")
    public List<UrlDto> allUserUrls(@PathVariable("id") Long id) {
        return urlService.getAllUrlUser(id);
    }

    @PostMapping("/{id}/edit")
    public void updateUrl(@PathVariable("id") Long id,
                          @RequestBody UpdateUrlRequest request) {
        urlService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        urlService.deleteById(id);
    }

    //using user`s id
    @GetMapping("/list/active/{id}")
    public List<UrlDto> ActiveUrls(@PathVariable("id") Long id){return  urlService.getActiveUrls(id);}
    //using user`s id
    @GetMapping("/list/inactive/{id}")
    public List<UrlDto> InactiveUrls(@PathVariable("id") Long id){return  urlService.getInactiveUrls(id);}
}

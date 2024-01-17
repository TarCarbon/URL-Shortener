package ua.goit.url;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/V1/urls")
@RequiredArgsConstructor
@Tag(name= "Url", description = "API to work with Urls")
public class UrlController {
    private final UrlService urlService;

    @GetMapping("/list")
    @Operation(summary = "Get all urls")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }

    @PostMapping("/create")
    public UrlDto createLink(@RequestBody CreateUrlRequest request) {
        return urlService.createUrl(getUsername(), request);
    @Operation(summary = "Create short url")
    public UrlDto createLink(@RequestBody CreateUrlRequest request) throws NotAccessibleException {
        UrlDto response = urlService.createUrl(request);
        ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        return response;
    }

    @GetMapping("/list/user")
    public List<UrlDto> allUserUrls() {
        return urlService.getAllUrlUser(getUsername());
    @GetMapping("/list/user/{id}")
    @Operation(summary = "Get all urls for current user")
    public List<UrlDto> allUserUrls(@PathVariable("id") Long id) {
        return urlService.getAllUrlUser(id);
    }

    @PostMapping("/edit/{id}")
    @PostMapping("/{id}/edit")
    @Operation(summary = "Url edit")
    public void updateUrl(@PathVariable("id") Long id,
                          @RequestBody UpdateUrlRequest request) {
        urlService.update(getUsername(), id, request);
    }

    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete url")
    public void deleteById(@PathVariable("id") Long id) {
        urlService.deleteById(getUsername(), id);
    }

    @GetMapping("/list/user/active")
    public List<UrlDto> ActiveUrls() {
        return urlService.getActiveUrls(getUsername());
    }

    //using user`s id
    @GetMapping("/list/active/{id}")
    @Operation(summary = "Get all active urls for current user")
    public List<UrlDto> ActiveUrls(@PathVariable("id") Long id){return  urlService.getActiveUrls(id);}
    //using user`s id
    @GetMapping("/list/inactive/{id}")
    @Operation(summary = "Get all inactive urls for current user")
    public List<UrlDto> InactiveUrls(@PathVariable("id") Long id){return  urlService.getInactiveUrls(id);}
    @GetMapping("/list/user/inactive")
    public List<UrlDto> InactiveUrls() {
        return urlService.getInactiveUrls(getUsername());
    }

    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        return principal.getUsername();
    }
}

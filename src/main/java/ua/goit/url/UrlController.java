package ua.goit.url;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;
import ua.goit.url.service.UrlServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/V1/urls")
@RequiredArgsConstructor
@Tag(name = "Url", description = "API to work with Urls")
public class UrlController {
    private final UrlService urlService;
    private final UrlServiceImpl urlServiceimpl;

    @GetMapping("/list")
    @Operation(summary = "Get all urls")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create short url")
    public UrlDto createLink(@RequestBody CreateUrlRequest request) {
        return urlService.createUrl(getUsername(), request);
    }

    @GetMapping("/list/user")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all urls for current user")
    public List<UrlDto> allUserUrls() {
        return urlService.getAllUrlUser(getUsername());
    }

    @PostMapping("/edit/{id}")
    @SecurityRequirement(name = "JWT")
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all active urls for current user")
    public List<UrlDto> ActiveUrls() {
        return urlService.getActiveUrls(getUsername());
    }

    @GetMapping("/list/user/inactive")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all inactive urls for current user")
    public List<UrlDto> InactiveUrls() {
        return urlService.getInactiveUrls(getUsername());
    }

    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        return principal.getUsername();
    }

    @GetMapping("/redirect/{shortUrl}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UrlEntity urlEntity = urlService.getByShortUrl(shortUrl);

        if (urlEntity != null && urlServiceimpl.isUrlAccessible(urlEntity.getUrl())) {
            urlEntity.setVisitCount(urlEntity.getVisitCount() + 1);
            urlService.updateUrl(urlEntity);

            return new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

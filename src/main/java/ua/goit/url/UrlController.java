package ua.goit.url;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;

import java.util.List;

@RestController
@RequestMapping("/V1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @GetMapping("/list")
    public List<UrlDto> urlList() {
        return urlService.listAll();
    }

    @PostMapping("/create")
    public UrlDto createLink(@RequestBody CreateUrlRequest request) {
        return urlService.createUrl(getUsername(), request);
    }

    @GetMapping("/list/user")
    public List<UrlDto> allUserUrls() {
        return urlService.getAllUrlUser(getUsername());
    }

    @PostMapping("/edit/{id}")
    public void updateUrl(@PathVariable("id") Long id,
                          @RequestBody UpdateUrlRequest request) {
        urlService.update(getUsername(), id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        urlService.deleteById(getUsername(), id);
    }

    @GetMapping("/list/user/active")
    public List<UrlDto> ActiveUrls() {
        return urlService.getActiveUrls(getUsername());
    }

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

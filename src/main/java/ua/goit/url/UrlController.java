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
import ua.goit.user.UserEntity;

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

    @GetMapping("/list/user")
    public List<UrlDto> allUserUrls() {
        UserDetails principal = getUserDetails();
        return urlService.getAllUrlUser(principal.getUsername());

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

    @PostMapping("/edit/{id}")
    public void updateUrl(@PathVariable("id") Long id,
                          @RequestBody UpdateUrlRequest request) {
        UserDetails principal = getUserDetails();
        urlService.update(principal.getUsername(), id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        UserDetails principal = getUserDetails();
        urlService.deleteById(principal.getUsername(), id);
    }

    @GetMapping("/list/user/active")
    public List<UrlDto> ActiveUrls(){
        UserDetails principal = getUserDetails();
        return  urlService.getActiveUrls(principal.getUsername());
    }

    @GetMapping("/list/user/inactive")
    public List<UrlDto> InactiveUrls(){
        UserDetails principal = getUserDetails();
        return  urlService.getInactiveUrls(principal.getUsername());
    }

    private UserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserDetails) context.getAuthentication().getPrincipal();
    }
}

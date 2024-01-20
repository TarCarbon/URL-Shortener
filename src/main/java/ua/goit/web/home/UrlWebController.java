package ua.goit.web.home;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/V2/urls")
@RequiredArgsConstructor
@Tag(name = "Url", description = "API to work with Urls")
public class UrlWebController {
    private final UrlServiceImpl service;

    @GetMapping("/index")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView homePage() {
        return new ModelAndView("index");
    }

    @GetMapping(value = "/list")
    @Operation(summary = "Get all urls")
    public ModelAndView allUrl() {
        ModelAndView result = new ModelAndView("all");
        result.addObject("urls", service.listAll());
//        result.addObject("username", "Maria");
        return result;
    }

    @GetMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView showCreatePage() {
        return new ModelAndView("create");
    }

    @PostMapping(value = "/create")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create short url")
    public ModelAndView create(CreateUrlRequest request) {
        String username = getUsername();
        service.createUrl(username, request);
        return new ModelAndView("all");
    }

    @GetMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView showEditPage() {
        return new ModelAndView("edit");
    }

    @PostMapping(value = "/edit/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Url edit")
    public ModelAndView editUrl(@RequestBody UpdateUrlRequest request, @PathVariable("id") Long id) {
        String username = getUsername();
        service.update(username, id, request);
        return new ModelAndView("all");
    }

    @PostMapping(value = "/delete/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete url")
    public ModelAndView delete(@PathVariable("id") Long id) {
        String username = getUsername();
        service.deleteById(username, id);
        return new ModelAndView("all");
    }

    @PostMapping(value = "/prolong/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Prolong")
    public ModelAndView prolong(@PathVariable("id") Long id) {
        String username = getUsername();
        service.prolongUrl(id);
        return new ModelAndView("all");
    }

    @GetMapping(value = "/list/user/active")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all active urls for current user")
    public ModelAndView showActiveUrls() {
        ModelAndView result = new ModelAndView("all");
//        String username = getUsername();
        List<UrlDto> activeUserUrls = service.getActiveUrls(getUsername());
        result.addObject("urls", activeUserUrls);
        result.addObject("username", "Maria");
        return result;
    }

    @GetMapping(value = "/list/user/inactive")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all inactive urls for current user")
    public ModelAndView showInactiveUrls() {
        ModelAndView result = new ModelAndView("all");
//        String username = getUsername();
        List<UrlDto> inactiveUserUrls = service.getInactiveUrls(getUsername());
        List<UrlDto> activeUserUrls = service.getActiveUrls(getUsername());
        result.addObject("urls", inactiveUserUrls);
        result.addObject("username", "Maria");
        return result;
    }


    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        return principal.getUsername();
    }
}
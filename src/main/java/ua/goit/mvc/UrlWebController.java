package ua.goit.mvc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;

import java.security.Principal;

@Controller
@RequestMapping("V2/urls")
@RequiredArgsConstructor
public class UrlWebController {
    private final UrlService urlService;
    private final UrlWebService urlWebService;

    @GetMapping()
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/user")
    public ModelAndView getIndexPageForUser(Principal principal) {
        ModelAndView result = new ModelAndView("index-user");
        result.addObject("username", principal.getName());
        return result;
    }

    @GetMapping("/list")
    public ModelAndView getAllLinks() {
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.listAll());
        return result;
    }

    @GetMapping("/list/active")
    public ModelAndView getAllActiveLinks() {
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.getActiveUrl());
        return result;
    }

    @GetMapping("/list/inactive")
    public ModelAndView getAllInactiveLinks() {
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.getInactiveUrl());
        return result;
    }

    @GetMapping("/list/user")
    public ModelAndView getAllUsersLinks(Principal principal) {
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getAllUrlUser(principal.getName()));
        return result;
    }

    @GetMapping("/list/user/active")
    public ModelAndView getAllUsersActiveLinks(Principal principal) {
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getActiveUrlUser(principal.getName()));
        return result;
    }

    @GetMapping("/list/user/inactive")
    public ModelAndView getAllUsersInactiveLinks(Principal principal) {
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getInactiveUrlUser(principal.getName()));
        return result;
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, Principal principal) {
        ModelAndView result = new ModelAndView("edit");
        result.addObject("username", principal.getName());
        result.addObject("urls", urlService.getById(id));
        result.addObject("id", id);
        return result;
    }

    @PostMapping(value = "/edit")
    public ModelAndView postEdit(@Valid @ModelAttribute UpdateUrlRequest updateUrlRequest,
                             BindingResult bindingResult,
                             @RequestParam("id") Long id,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
             return urlWebService.getEditModelAndViewWithErrors(bindingResult, updateUrlRequest, id, principal);
        }
        return urlWebService.updateUrl(principal, id, updateUrlRequest);
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        urlService.deleteById(principal.getName(), id);
        return "redirect:/V2/urls/list/user";
    }

    @GetMapping(value = "/prolongation/{id}")
    public String prolongation(@PathVariable("id") Long id, Principal principal) {
        urlService.prolongation(principal.getName(), id);
        return "redirect:/V2/urls/list/user";
    }

    @GetMapping(value = "/create")
    public ModelAndView create(Principal principal) {
        ModelAndView result = new ModelAndView("create");
        result.addObject("username", principal.getName());
        return result;
    }

    @PostMapping(value = "/create")
    public ModelAndView postCreate(@Valid @ModelAttribute CreateUrlRequest createUrlRequest,
                             BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            return urlWebService.getCreateModelAndViewWithErrors(bindingResult, principal);
        }
        return urlWebService.createUrl(principal, createUrlRequest);
    }
}
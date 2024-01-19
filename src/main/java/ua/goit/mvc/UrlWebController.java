package ua.goit.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.service.UrlService;

import java.security.Principal;

@Controller
@RequestMapping("V2/urls")
@RequiredArgsConstructor
public class UrlWebController {
    private final UrlService urlService;
    @GetMapping()
    public String getIndexPage(){
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getIndexPageForRegister(Principal principal){
        ModelAndView result = new ModelAndView("index-register");
        result.addObject("username", principal.getName());
        return result;
    }

    @GetMapping("/list")
    public ModelAndView getAllLinks(){
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.listAll());
        return result;
    }

    @GetMapping("/list/active")
    public ModelAndView getAllActiveLinks(){
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.getActiveUrl());
        return result;
    }

    @GetMapping("/list/inactive")
    public ModelAndView getAllInactiveLinks(){
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.getInactiveUrl());
        return result;
    }

    @GetMapping("/list/user")
    public ModelAndView getAllUsersLinks(Principal principal){
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getAllUrlUser(principal.getName()));
        return result;
    }

     @GetMapping("/list/user/active")
    public ModelAndView getAllUsersActiveLinks(Principal principal){
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getActiveUrlUser(principal.getName()));
        return result;
    }

    @GetMapping("/list/user/inactive")
    public ModelAndView getAllUsersInactiveLinks(Principal principal){
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getInactiveUrlUser(principal.getName()));
        return result;
    }
}

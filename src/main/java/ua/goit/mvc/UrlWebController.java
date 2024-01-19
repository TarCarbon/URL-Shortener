package ua.goit.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ModelAndView getIndexPage(){
        return new ModelAndView("index");
    }

    @GetMapping("/list")
    public ModelAndView getAllLinks(HttpServletResponse response, HttpServletRequest request, Principal principal){
        ModelAndView result = new ModelAndView("all-guest");
        result.addObject("userUrls", urlService.listAll());
        return result;
    }
}

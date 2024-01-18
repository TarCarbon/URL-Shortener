package ua.goit.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.service.UrlService;

@Controller
@RequestMapping("V2/urls")
@RequiredArgsConstructor
public class UrlWebController {
    private final UrlService urlService;
    @GetMapping()
    public ModelAndView getIndexPage(){
        return new ModelAndView("index");
    }

    @GetMapping("/all")
    public ModelAndView getAllLinks(){
        return new ModelAndView("all");
    }
}

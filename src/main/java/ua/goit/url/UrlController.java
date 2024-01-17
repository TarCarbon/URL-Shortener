package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.service.UrlService;

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

    @GetMapping("/list/user")
    public ModelAndView allUserUrls(Long id) {
        ModelAndView result = new ModelAndView("all");
        List<UrlDto> urlDtos = urlService.getAllUrlUser(id);
        result.addObject("urls", urlDtos);
        return result;
    }
}

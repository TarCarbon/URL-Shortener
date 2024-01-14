package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.service.UrlService;

@Validated
@Controller
//@RequestMapping("/")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlMapper urlMapper;

    @RequestMapping(value = "/V1/list", method = {RequestMethod.GET})
    public ModelAndView urlList(){
        ModelAndView result = new ModelAndView("template");
        result.addObject("urls", urlMapper.toUrlResponses(urlService.listAll()));
        return result;
    }
}

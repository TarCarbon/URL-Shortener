package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.service.UrlService;
import ua.goit.user.UserEntity;

@Validated
@Controller
@RequestMapping("/V1/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlMapper urlMapper;

    @RequestMapping(value = "/V1/urls/list", method = {RequestMethod.GET})
    public ModelAndView urlList() {
        ModelAndView result = new ModelAndView("template");
        result.addObject("urls", urlMapper.toUrlResponses(urlService.listAll()));
        return result;
    }

    @RequestMapping(value = "V1/urls/create", method = {RequestMethod.POST})
    public ModelAndView createUrl(
            @RequestParam(value = "url") String url,
            @RequestParam(value = "description") String description) {
        UrlDto dto = new UrlDto();
        dto.setUrl(url);
        dto.setShortUrl("method for create short url");
        dto.setUser(new UserEntity());      // need some logic for setting user here
        dto.setDescription(description);
        dto.setVisitCount(0);
        urlService.add(dto);
        return urlList();
    }

    @RequestMapping(value = "V1/urls/edit", method = {RequestMethod.GET})
    public ModelAndView getUrlForEdit(@RequestParam(value = "short_url") String shortUrl) {
        ModelAndView result = new ModelAndView("V1/urls/update");
        result.addObject("urls", urlMapper.toUrlResponse(urlService.getByShortUrl(shortUrl)));
        return result;
    }

    @RequestMapping(value = "/V1/urls/update", method = {RequestMethod.POST})
    public ModelAndView updateUrl(
            @RequestParam(value = "short_url") String shortUrl,
            @RequestParam(value = "description") String description
    ) {
        UrlDto dto = new UrlDto();
        dto.setShortUrl(shortUrl);
        dto.setDescription(description);
        urlService.update(dto);
        return getUrlForEdit(shortUrl);
    }

    @DeleteMapping("V1/urls/delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public ModelAndView deleteUrlByShortUrl(@RequestParam(value = "short_url") String shortUrl){
        urlService.deleteByShortUrl(shortUrl);
        return urlList();
    }
}

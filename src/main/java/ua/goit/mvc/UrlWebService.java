package ua.goit.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlService;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlWebService {
    private final UrlService urlService;

    public ModelAndView getEditModelAndViewWithErrors(BindingResult bindingResult,
                                                      UpdateUrlRequest updateUrlRequest,
                                                      Long id,
                                                      Principal principal) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ModelAndView result = new ModelAndView("edit");
        result.addObject("errors", errors);
        result.addObject("username", principal.getName());
        result.addObject("urls", updateUrlRequest);
        result.addObject("id", id);
        return result;
    }

    public ModelAndView updateUrl(Principal principal, Long id, UpdateUrlRequest updateUrlRequest) {
        try {
            urlService.update(principal.getName(), id, updateUrlRequest);
        } catch (Exception e) {
            ModelAndView result = new ModelAndView("edit");
            result.addObject("errors", e.getMessage());
            result.addObject("username", principal.getName());
            result.addObject("urls", updateUrlRequest);
            result.addObject("id", id);
            return result;
        }
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getActiveUrlUser(principal.getName()));
        result.addObject("userUrlsInactive", urlService.getInactiveUrlUser(principal.getName()));
        return result;
    }

    public ModelAndView getCreateModelAndViewWithErrors(BindingResult bindingResult,
                                                        Principal principal) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ModelAndView result = new ModelAndView("create");
        result.addObject("errors", errors);
        result.addObject("username", principal.getName());
        return result;
    }

    public ModelAndView createUrl(Principal principal, CreateUrlRequest createUrlRequest) {
        try {
            urlService.createUrl(principal.getName(), createUrlRequest);
        } catch (Exception e) {
            ModelAndView result = new ModelAndView("create");
            result.addObject("errors", e.getMessage());
            result.addObject("username", principal.getName());
            return result;
        }
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", principal.getName());
        result.addObject("userUrls", urlService.getActiveUrlUser(principal.getName()));
        result.addObject("userUrlsInactive", urlService.getInactiveUrlUser(principal.getName()));
        return result;
    }
}

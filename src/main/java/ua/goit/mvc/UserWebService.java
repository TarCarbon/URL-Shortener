package ua.goit.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.url.service.UrlService;
import ua.goit.user.CreateUserRequest;
import ua.goit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWebService {
    private final UserService userService;
    private final UrlService urlService;

    public ModelAndView registerUser(CreateUserRequest userRequest) {
        try {
            userService.registerUser(userRequest);
        } catch (Exception e) {
            ModelAndView result = new ModelAndView("register");
            result.addObject("errors", e.getMessage());
            return result;
        }
        return new ModelAndView("success");
    }

    public ModelAndView loginUser(CreateUserRequest userRequest) {
        try {
            userService.loginUser(userRequest);
        } catch (Exception e) {
            ModelAndView result = new ModelAndView("login");
            result.addObject("errors", "Wrong username or password");
            return result;
        }
        ModelAndView result = new ModelAndView("all-user");
        result.addObject("username", getUsername());
        result.addObject("userUrls", urlService.getAllUrlUser(getUsername()));
        return result;
    }

    public ModelAndView getModelAndViewWithErrors(BindingResult bindingResult, ModelAndView result) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        result.addObject("errors", errors);
        return result;
    }

    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        return principal.getUsername();
    }
}

package ua.goit.mvc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.user.CreateUserRequest;
import ua.goit.user.service.UserService;

@Controller
@RequestMapping("V2/user")
@RequiredArgsConstructor
public class UserWebController {
    private final UserService userService;

    @GetMapping("/register")
    public ModelAndView getRegisterUser(){
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public void postRegisterUser(@Valid @ModelAttribute CreateUserRequest userRequest) {
        userService.registerUser(userRequest);
       // return new ModelAndView("index");
    }

    @GetMapping("/login")
    public ModelAndView getLoginUser(){
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public String postLoginUser(@Valid @ModelAttribute CreateUserRequest userRequest) {
        return userService.loginUser(userRequest);
    }
}
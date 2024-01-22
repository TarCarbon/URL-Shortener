package ua.goit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.goit.user.service.UserService;

@RestController
@RequestMapping("V1/user")
@RequiredArgsConstructor
@Tag(name= "User", description = "User API")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Allow to register new User")
    public void registerUser(@Valid @RequestBody CreateUserRequest userRequest) {
        userService.registerUser(userRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Allow user to login")
    public String loginUser(@Valid @RequestBody CreateUserRequest userRequest) {
        return userService.loginUser(userRequest);
    }

    @GetMapping("/logout")
    @Operation(summary = "User login", description = "Allow user to logout")
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        userService.logoutUser(request, response);
    }
}

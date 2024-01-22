package ua.goit.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.goit.user.CreateUserRequest;

import ua.goit.user.dto.UserDto;

public interface UserService {
    public UserDto findByUsername(String username);
    void registerUser(CreateUserRequest userRequest);
    String loginUser(CreateUserRequest userRequest);
    void logoutUser(HttpServletRequest request, HttpServletResponse response);
}

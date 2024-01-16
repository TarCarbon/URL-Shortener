package ua.goit.user.service;

import ua.goit.user.CreateUserRequest;

public interface UserService {
    void registerUser(CreateUserRequest userRequest);
}

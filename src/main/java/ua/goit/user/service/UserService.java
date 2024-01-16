package ua.goit.user.service;

import ua.goit.user.dto.UserDto;

public interface UserService {
    public UserDto findByUsername(String username);
}

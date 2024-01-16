package ua.goit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.user.UserEntity;
import ua.goit.user.dto.UserDto;
import ua.goit.user.mapper.UserMapper;
import ua.goit.user.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public UserDto findByUsername(String username){
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if(userEntity.isPresent()){
            return userMapper.toUserDto(userEntity.get());
        } else {
            throw new NoSuchElementException("there isn't user with username: " + username);
        }

    }
}

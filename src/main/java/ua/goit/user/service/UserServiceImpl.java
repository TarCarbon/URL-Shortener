package ua.goit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.goit.user.CreateUserRequest;
import ua.goit.user.Role;
import ua.goit.user.UserAlreadyExistException;
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

    @Override
    public void registerUser(CreateUserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException(username);
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                //TODO change when add JWT
                //.password(encoder.encode(password))
                .password(password)
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }
}

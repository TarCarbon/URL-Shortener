package ua.goit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.goit.user.CreateUserRequest;
import ua.goit.user.Role;
import ua.goit.user.UserAlreadyExistException;
import ua.goit.user.UserEntity;
import ua.goit.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    //TODO change when add JWT
    //private final PasswordEncoder encoder;

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

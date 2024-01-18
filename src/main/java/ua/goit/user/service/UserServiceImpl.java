package ua.goit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.goit.user.CreateUserRequest;
import ua.goit.user.Role;
import ua.goit.user.UserAlreadyExistException;
import ua.goit.user.UserEntity;
import ua.goit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService, UserDetailsService {
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
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity user= findByUsername(username).orElseThrow(()->new UsernameNotFoundException(
               String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}

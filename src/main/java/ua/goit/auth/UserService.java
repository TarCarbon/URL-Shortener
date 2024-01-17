package ua.goit.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.goit.user.UserEntity;
import ua.goit.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserEntity findByUsername(String username) {
        Optional<UserEntity> user = repository.findByUsername(username);
        return user.orElse(null);
    }

    public void saveUser(UserEntity user) {
        repository.save(user);
    }
}
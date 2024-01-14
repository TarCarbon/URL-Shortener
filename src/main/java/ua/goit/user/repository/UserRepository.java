package ua.goit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.goit.user.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}

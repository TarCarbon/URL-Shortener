package ua.goit.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.goit.url.UrlEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.expirationDate > :currentDateTime")
    List<UrlEntity> findActiveUrlsByUserId(@Param("userId") Long userId, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.expirationDate <= :currentDateTime")
    List<UrlEntity> findInactiveUrlsByUserId(@Param("userId") Long userId, @Param("currentDateTime") LocalDateTime currentDateTime);
}
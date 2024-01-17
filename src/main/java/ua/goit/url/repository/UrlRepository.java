package ua.goit.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.goit.url.UrlEntity;

import java.util.List;

import java.time.LocalDateTime;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.expirationDate > :currentDateTime")
    List<UrlEntity> findActiveUrlsByUserId(@Param("userId") Long userId, @Param("currentDateTime") LocalDateTime currentDateTime);

    List<UrlEntity> findByUserId(Long id);

    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.expirationDate <= :currentDateTime")
    List<UrlEntity> findInactiveUrlsByUserId(@Param("userId") Long userId, @Param("currentDateTime") LocalDateTime currentDateTime);
}

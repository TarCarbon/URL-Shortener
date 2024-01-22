package ua.goit.url.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.goit.url.UrlEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.expirationDate > :currentDate")
    List<UrlEntity> findActiveUrlsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    List<UrlEntity> findByUserId(Long id);

    Optional<UrlEntity> findByShortUrl(String shortUrl);

    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.expirationDate <= :currentDate")
    List<UrlEntity> findInactiveUrlsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT u FROM UrlEntity u WHERE u.expirationDate > :currentDate")
    List<UrlEntity> findActiveUrls(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT u FROM UrlEntity u WHERE u.expirationDate <= :currentDate")
    List<UrlEntity> findInactiveUrls(@Param("currentDate") LocalDate currentDate);
}

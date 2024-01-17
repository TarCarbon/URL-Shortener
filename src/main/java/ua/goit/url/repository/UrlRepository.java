package ua.goit.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ua.goit.url.UrlEntity;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    List<UrlEntity> findByUserId(Long id);

}

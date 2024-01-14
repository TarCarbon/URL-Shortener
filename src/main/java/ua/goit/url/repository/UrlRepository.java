package ua.goit.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.goit.url.UrlEntity;

import java.util.List;


@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, String> {
    @Query(nativeQuery = true, value =
            "SELECT shortUrl FROM urls")
    List<String> allShortUrls();
}

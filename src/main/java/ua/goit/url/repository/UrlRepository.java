package ua.goit.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.goit.url.UrlEntity;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, String> {

}

package ua.goit.url;


import jakarta.persistence.*;
import lombok.Data;
import ua.goit.user.UserEntity;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "urls")
public class UrlEntity {

    public UrlEntity(String shortUrl, String url, String description, UserEntity user, LocalDateTime createdDate
            , LocalDateTime expirationDate, int visitCount) {
        this.shortUrl = shortUrl;
        this.url = url;
        this.description = description;
        this.user = user;
        this.createdDate = createdDate;
        this.expirationDate = expirationDate;
        this.visitCount = visitCount;
    }

    private static final int VALID_DAYS = 30;

    @Id
    private String shortUrl;

    @Column(nullable = false)
    private String url;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private int visitCount;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.expirationDate = this.createdDate.plusDays(VALID_DAYS);
    }
}
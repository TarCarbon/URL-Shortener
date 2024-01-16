package ua.goit.url;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.goit.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "urls")
@NoArgsConstructor
@AllArgsConstructor
public class UrlEntity {

    private static final int VALID_DAYS = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
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

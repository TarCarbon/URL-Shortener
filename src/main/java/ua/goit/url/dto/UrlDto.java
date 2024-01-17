package ua.goit.url.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.goit.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDto {
    private Long id;
    private String shortUrl;
    private String url;
    private String description;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime expirationDate;
    private int visitCount;
}

package ua.goit.url.response;

import lombok.Data;
import ua.goit.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class UrlResponse {
    private BigDecimal id;
    private String shortUrl;
    private String url;
    private String description;
    private UserEntity user;
    private LocalDateTime createdDate;
    private LocalDateTime expirationDate;
    private int visitCount;
}

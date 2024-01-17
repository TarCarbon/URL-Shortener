package ua.goit.url.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UrlResponse {
    private Long id;
    private String shortUrl;
    private String url;
    private String description;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime expirationDate;
    private int visitCount;
}
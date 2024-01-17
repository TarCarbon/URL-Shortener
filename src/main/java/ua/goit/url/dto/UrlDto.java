package ua.goit.url.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

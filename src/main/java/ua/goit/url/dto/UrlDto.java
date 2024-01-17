package ua.goit.url.dto;


import lombok.Data;



import java.time.LocalDateTime;

@Data
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

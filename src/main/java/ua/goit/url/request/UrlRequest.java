package ua.goit.url.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.goit.user.UserEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {
    private BigDecimal id;
    private String shortUrl;
    private String url;
    private String description;
    private UserEntity user;
    private int visitCount;

    public UrlRequest(String shortUrl, String description, int visitCount) {
        this.shortUrl = shortUrl;
        this.description = description;
        this.visitCount = visitCount;
    }
}

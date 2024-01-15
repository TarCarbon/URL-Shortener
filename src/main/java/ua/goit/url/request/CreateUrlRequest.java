package ua.goit.url.request;

import ua.goit.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateUrlRequest extends UrlRequest{
    public CreateUrlRequest(BigDecimal id, String shortUrl, String url, String description, UserEntity user, int visitCount) {
        super(id, shortUrl, url, description, user, 0); // id, short url and user should be changed to specified methods
    }
}

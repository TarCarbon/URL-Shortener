package ua.goit.url.request;

import ua.goit.user.UserEntity;

public class UpdateUrlRequest extends UrlRequest{
    public UpdateUrlRequest(String shortUrl, String description, int visitCount) {
        super(shortUrl, description,0);
    }
}

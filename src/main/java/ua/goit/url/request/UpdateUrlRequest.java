package ua.goit.url.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.goit.user.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUrlRequest {
    private String shortUrl;
    private String url;
    private String description;
}

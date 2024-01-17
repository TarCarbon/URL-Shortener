package ua.goit.url.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class UrlRequest {
    /*@Size(max = 8)
    private String shortUrl;*/

    @Size(max = 255)
    private String url;

    @Size(max = 1000)
    private String description;

/*    @Size(min = 2, max = 50)
    private String username;*/
}

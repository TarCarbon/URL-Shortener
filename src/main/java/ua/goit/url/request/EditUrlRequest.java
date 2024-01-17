package ua.goit.url.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUrlRequest {
    private String shortUrl;
    private String url;
    private String description;
}

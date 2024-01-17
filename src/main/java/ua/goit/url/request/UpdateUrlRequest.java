package ua.goit.url.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for update Url")
public class UpdateUrlRequest {
    private String shortUrl;
    private String url;
    private String description;
}

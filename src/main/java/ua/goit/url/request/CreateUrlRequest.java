package ua.goit.url.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create Url")
public class CreateUrlRequest {
    @NotBlank(message = "Url is required")
    @Size(max = 255, message = "Max size for url is 255 characters")
    private String url;

    @Size(max = 1000, message = "Max size for description is 1000 characters")
    private String description;
}

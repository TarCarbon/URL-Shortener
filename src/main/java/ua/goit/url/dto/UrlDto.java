package ua.goit.url.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto for Url")
public class UrlDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String shortUrl;
    private String url;
    private String description;
    private String username;
    private LocalDate createdDate;
    private LocalDate expirationDate;
    private int visitCount;
}

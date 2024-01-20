package ua.goit.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Credentials")
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username must be at least 2 characters long")
    @Schema(description = "Username must be at least 2 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "Password should have 8 or more characters and contains numbers, " +
            "letters in upper case and letters in lower case")
    @Schema(description = "Password should have 8 or more characters and contains numbers, " +
            "letters in upper case and letters in lower case")
    private String password;
}

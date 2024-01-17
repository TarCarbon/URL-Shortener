package ua.goit.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegistrationResponse {

    public enum Error {
        OK,
        USER_ALREADY_EXISTS,
        INVALID_USERNAME,
        INVALID_PASSWORD,
        INVALID_CONFIRM_PASSWORD
    }

    private Error error;

    public static RegistrationResponse success() {
        return builder().error(Error.OK).build();
    }

    public static RegistrationResponse failed(Error error) {
        return builder().error(error).build();
    }
}
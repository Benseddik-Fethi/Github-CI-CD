package fr.benseddik.planning.error.exception;


import fr.benseddik.planning.error.record.ExceptionWithErrorResponse;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * @author fethi
 * @date 02/03/2023
 * @time 13:55
 */
@Getter
@ToString
public class UsernameNotFoundException extends RuntimeException implements ExceptionWithErrorResponse {

    private static final String DEFAULT_MESSAGE = "Username not found";
    private static final String DEFAULT_CODE = "username_not_found";
    @Serial
    private static final long serialVersionUID = -6571178747214202448L;

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final int status;
    private final String url;

    public UsernameNotFoundException() {
        this.message = DEFAULT_MESSAGE;
        this.code = DEFAULT_CODE;
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.status = httpStatus.value();
        this.url = "";
    }
}
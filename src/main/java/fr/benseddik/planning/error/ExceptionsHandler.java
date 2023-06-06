package fr.benseddik.planning.error;

import fr.benseddik.planning.error.exception.EmailExistException;
import fr.benseddik.planning.error.exception.UsernameExistException;
import fr.benseddik.planning.error.exception.UsernameNotFoundException;
import fr.benseddik.planning.error.record.ErrorResponse;
import fr.benseddik.planning.error.record.ExceptionWithErrorResponse;
import fr.benseddik.planning.error.record.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

/**
 * ExceptionsHandler is the class that handle all the exception from the application for witch we want to send an error response to the client.
 *
 * @author Fethi Benseddik
 */
@ControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {


    /**
     * Handle the MethodArgumentNotValidException to automatically send an error response to the client.
     *
     * @param ex      the exception MethodArgumentNotValidException
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the error response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        log.error("Method argument not valid: {}", ex.getMessage());
        List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldError(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getCode()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handle the UsernameAlreadyInUseException to automatically send an error response to the client.
     *
     * @param ex the exception UsernameAlreadyInUseException
     * @return the error response entity
     */
    @ExceptionHandler(UsernameExistException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameExistException(UsernameExistException ex) {
        log.error("Username already exist: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handle the EmailAlreadyExistException to automatically send an error response to the client.
     *
     * @param ex the exception EmailAlreadyExistException
     * @return the error response entity
     */
    @ExceptionHandler(EmailExistException.class)
    protected ResponseEntity<ErrorResponse> handleEmailExistException(EmailExistException ex) {
        log.error("Email already exist: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("Username not found: {}", ex.getMessage());
        return handleExceptionWithErrorResponse(ex);
    }

    /**
     * Handles exceptions that involve a custom error response.
     * It logs an error with the name of the exception class and its message and returns an HTTP response with a 400 Bad Request status code and a response body containing the error details.
     *
     * @param ex The exception with a custom error response
     * @return The HTTP response with a 400 Bad Request status code and a response body containing the error details
     */
    protected ResponseEntity<ErrorResponse> handleExceptionWithErrorResponse(ExceptionWithErrorResponse ex) {
        log.error("{} already exist: {}", ex.getClass().getSimpleName(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                ex.getHttpStatus().getReasonPhrase(),
                ex.getStatus(),
                ex.getUrl(),
                Instant.now());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}

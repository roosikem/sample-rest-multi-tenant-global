package com.lkup.accounts.exceptions;

import com.lkup.accounts.dto.error.ErrorDto;
import com.lkup.accounts.exceptions.apikey.APIKeyNotFoundException;
import com.lkup.accounts.exceptions.apikey.APIKeyServiceException;
import com.lkup.accounts.exceptions.aws.AwsS3UploadException;
import com.lkup.accounts.exceptions.environment.EnvironmentNotFoundException;
import com.lkup.accounts.exceptions.environment.EnvironmentServiceException;
import com.lkup.accounts.exceptions.role.RoleNotFoundException;
import com.lkup.accounts.exceptions.role.RoleServiceException;
import com.lkup.accounts.exceptions.user.UserNotFoundException;
import com.lkup.accounts.exceptions.user.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIKeyNotFoundException.class)
    public ResponseEntity<Object> handleAPIKeyNotFoundException(APIKeyNotFoundException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIKeyServiceException.class)
    public ResponseEntity<Object> handleAPIKeyServiceException(APIKeyServiceException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleServiceException.class)
    public ResponseEntity<Object> handleRoleServiceException(RoleServiceException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<Object> handleAuthorizationServiceException(AuthorizationServiceException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.FORBIDDEN, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedExceptions(AccessDeniedException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(EnvironmentNotFoundException.class)
    public ResponseEntity<Object> handleUserServiceException(EnvironmentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EnvironmentServiceException.class)
    public ResponseEntity<Object> handleUserServiceException(EnvironmentServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AwsS3UploadException.class)
    public ResponseEntity<ErrorDto> handleAwsS3UploadException(AwsS3UploadException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        StringBuilder strBuilder = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();

            } catch (ClassCastException ex) {
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();
            strBuilder.append(String.format("%s: %s\n", fieldName, message));
        });

        ErrorDto message = buildMessage(HttpStatus.BAD_REQUEST, strBuilder.substring(0, strBuilder.length() - 1), request);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        if (Objects.nonNull(ex.getCause())) {
            errorMessage += "-" + ex.getCause().getLocalizedMessage();
        }
        ErrorDto message = buildMessage(HttpStatus.BAD_REQUEST, errorMessage, request);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTenantIdException.class)
    public ResponseEntity<ErrorDto> handleInvalidTenantIdException(InvalidTenantIdException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTeamIdException.class)
    public ResponseEntity<ErrorDto> handleInvalidTeamIdException(InvalidTeamIdException ex, WebRequest request) {
        ErrorDto message = buildMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(message, HttpStatus.BAD_GATEWAY);
    }

    private static ErrorDto buildMessage(HttpStatus internalServerError, String ex, WebRequest request) {
        return new ErrorDto(
                internalServerError.value(),
                new Date(),
                ex,
                request.getDescription(false));
    }

}

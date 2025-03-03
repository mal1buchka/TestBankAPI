package kg.test.test_project_for_bank.ExceptionHandlers;

import jakarta.servlet.http.HttpServletRequest;
import kg.test.test_project_for_bank.Exceptions.UserExceptions.SuchUserNotFoundExeception;
import kg.test.test_project_for_bank.Exceptions.UserExceptions.UserAlreadyExistExeception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler extends BaseExceptionHandler{
    @ExceptionHandler(SuchUserNotFoundExeception.class)
    public ResponseEntity<DetailedError> handleSuchUserNotFoundExeception(SuchUserNotFoundExeception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
    @ExceptionHandler(UserAlreadyExistExeception.class)
    public ResponseEntity<DetailedError> handleUserAlreadyExistExeception(UserAlreadyExistExeception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DetailedError> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occured: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_ERROR(500)");
    }
}

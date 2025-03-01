package kg.test.test_project_for_bank.ExceptionHandlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
public abstract class BaseExceptionHandler {
    protected ResponseEntity<DetailedError> buildErrorResponse(
            Exception ex,
            HttpServletRequest request,
            HttpStatus status,
            String errorCode

    ) {
        LocalDateTime now = LocalDateTime.now();
        log.error("Ошибка произошла: {}, Метод: {}, Путь: {}",
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );

        DetailedError errorDetail = DetailedError.builder()
                .timestamp(now)
                .message(ex.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .errorCode(errorCode)
                .status(status)
                .build();

        return new ResponseEntity<>(errorDetail, status);
    }
    @ExceptionHandler (ConstraintViolationException.class)
    public ResponseEntity<DetailedError> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DetailedError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }

}

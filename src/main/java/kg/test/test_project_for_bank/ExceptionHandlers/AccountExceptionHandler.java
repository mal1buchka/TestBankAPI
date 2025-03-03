package kg.test.test_project_for_bank.ExceptionHandlers;

import jakarta.servlet.http.HttpServletRequest;
import kg.test.test_project_for_bank.Exceptions.AccountExceptions.IllegalAccountOperationException;
import kg.test.test_project_for_bank.Exceptions.AccountExceptions.SuchAcccountForUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler extends BaseExceptionHandler{
    @ExceptionHandler(IllegalAccountOperationException.class)
    ResponseEntity<DetailedError> handleIllegalAccountOperationException(IllegalAccountOperationException e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.BAD_REQUEST, "ILLEGAL_ACCOUNT_OPERATION");
    }
    @ExceptionHandler(SuchAcccountForUserNotFoundException.class)
    ResponseEntity<DetailedError> handleSuchAcccountForUserNotFoundException(SuchAcccountForUserNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<DetailedError> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT");
    }
}

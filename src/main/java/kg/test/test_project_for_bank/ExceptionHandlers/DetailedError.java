package kg.test.test_project_for_bank.ExceptionHandlers;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DetailedError(LocalDateTime timestamp, String message, String errorCode, String path, String method, HttpStatus status){}

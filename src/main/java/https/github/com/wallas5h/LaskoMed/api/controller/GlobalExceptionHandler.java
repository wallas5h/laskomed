package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.UUID;

// własna obsługa błędów
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)  // zapis sugeruje ze ta obsługa błędów jest najważniejsza, użyty w pierwszej kolejnosci
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Map< Class<?>, HttpStatus> EXCEPTION_STATUS= Map.of(
      BindException.class, HttpStatus.BAD_REQUEST,
      ConstraintViolationException.class, HttpStatus.BAD_REQUEST,
      IllegalArgumentException.class, HttpStatus.BAD_REQUEST,
      EntityNotFoundException.class, HttpStatus.NOT_FOUND
  );

  // nadpisanie domyślnej metody obsługującej błędy wraz ze swoimi zmianami
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      @NonNull Exception ex,
      Object body,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode statusCode,
      @NonNull WebRequest request
  ) {
    final String errorId= UUID.randomUUID().toString();
    log.error("Exception: ID={}, HttpStatus={}", errorId, statusCode, ex);

    return super.handleExceptionInternal(
        ex,
        ExceptionMessage.of(errorId),  // instead body
        headers,
        statusCode,
        request
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handle (Exception exception){
    ResponseEntity<?> responseEntity = doHandle(exception, getHttpStatusFromException(exception.getClass()));
    return responseEntity;
  }

  private ResponseEntity<?> doHandle(Exception exception, HttpStatus status){
    final String errorId= UUID.randomUUID().toString();
    log.error("Exception: ID={}, HttpStatus={}", errorId, status, exception);

    return ResponseEntity
        .status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ExceptionMessage.of(errorId));
  }

  private HttpStatus getHttpStatusFromException(final Class<?> exceptionClass) {
    return EXCEPTION_STATUS.getOrDefault(
        exceptionClass,
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}

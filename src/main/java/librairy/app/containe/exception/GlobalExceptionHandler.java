package librairy.app.containe.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import librairy.app.containe.exception.model.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ExceptionBody> handleApiException(
      ApiException exception, HttpServletRequest request) {
    return ResponseEntity.status(exception.getStatus().value())
        .body(
            new ExceptionBody(
                exception.getStatus().value(),
                exception.getStatus().getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ExceptionBody> handleNotFound(
      NotFoundException exception, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ExceptionBody(
                404, "Not Found", exception.getMessage(), request.getRequestURI(), Instant.now()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionBody> handleBadRequest(
      BadRequestException exception, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ExceptionBody(
                400,
                "Bad Request",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()));
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ExceptionBody> handleForbidden(
      ForbiddenException exception, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            new ExceptionBody(
                403, "Forbidden", exception.getMessage(), request.getRequestURI(), Instant.now()));
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ExceptionBody> handleValidationExceptions(
      Exception exception, HttpServletRequest request) {
    return ResponseEntity.badRequest()
        .body(
            new ExceptionBody(
                400,
                "Bad Request",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionBody> handleException(
      Exception exception, HttpServletRequest request) {
    return ResponseEntity.status(500)
        .body(
            new ExceptionBody(
                500,
                "An internal error has occurred",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now()));
  }
}

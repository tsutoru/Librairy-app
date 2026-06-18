package librairy.app.containe.exception;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
  private final HttpStatus status;

  public ApiException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public ApiException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class ExceptionBody {
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
  }
}

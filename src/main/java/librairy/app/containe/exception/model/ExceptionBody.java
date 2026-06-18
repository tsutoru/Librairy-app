package librairy.app.containe.exception.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {
  private int status;
  private String error;
  private String message;
  private String path;
  private Instant timestamp;
}

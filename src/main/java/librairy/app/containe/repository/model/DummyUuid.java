package librairy.app.containe.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import librairy.app.containe.PojaGenerated;
import lombok.Getter;
import lombok.Setter;

@PojaGenerated
@Entity
@Getter
@Setter
public class DummyUuid {
  @Id private String id;
}

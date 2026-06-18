package librairy.app.containe.Author.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "author")
@Getter
@Setter
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String firstName;
  private String lastName;
  private String biography;
  private String nationality;

  public Author() {}

  public Author(
      UUID id, String firstName, String lastName, String biography, String nationality) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.biography = biography;
    this.nationality = nationality;
  }
}

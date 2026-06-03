package librairy.app.containe.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author {

    private int id;
    private String firstName;
    private String lastName;
    private String biography;
    private String nationality;

    public Author() {}

    public Author(int id, String firstName, String lastName,
                  String biography, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.nationality = nationality;
    }
}
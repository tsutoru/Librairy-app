package librairy.app.containe.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
public class Book {
    @Id


    private String title;
    private String description;
    private Double price;
    private LocalDate publishDate;
    private String isbn;
    private int categoryId;

}

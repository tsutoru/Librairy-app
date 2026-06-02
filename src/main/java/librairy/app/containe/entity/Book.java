package librairy.app.containe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Book {
    private int Id;
    private String Title;
    private String Description;
    private Double Price;
    private LocalDate PublishDate;
    private String ISBN;
    private int CategoryId;

}

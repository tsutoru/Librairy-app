package librairy.app.containe.book.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookExternalDTO {
  private String id;
  private String title;
  private String description;
  private Double price;
  private String publicationDate;
  private String isbn;
  private String categoryName;
  private String authorName;
  private String publisher;
  private Integer pageCount;
  private String coverImageUrl;
}

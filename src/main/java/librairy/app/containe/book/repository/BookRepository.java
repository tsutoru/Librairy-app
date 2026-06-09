package librairy.app.containe.book.repository; 
import librairy.app.containe.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository
 extends JpaRepository<Book, Integer> {
    
 }

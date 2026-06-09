package librairy.app.containe.bookCopy.service;
import java.util.ArrayList;
import librairy.app.containe.entity.CopyStatus;
import java.util.List;
import org.springframework.stereotype.Service;
import librairy.app.containe.entity.BookCopy;


@Service 
public class BookCopyService { 
    private final List<BookCopy> copies = new ArrayList<>();

public BookCopy create(BookCopy copy) {
     copies.add(copy); 
     return copy;
     }

public List<BookCopy> getAll() { 
    return copies;
 }

 public BookCopy getById(int id) {
    return copies.stream() 
                 .filter(copy -> copy.getId() == id) 
                 .findFirst() 
                 .orElse(null);
                
            }

public List<BookCopy> getAvailable(Integer bookId) {
     return copies.stream() 
                .filter(copy -> 
                        copy.getStatus() == CopyStatus.AVAILABLE &&
                        (bookId == null || 
                         copy.getBook().getId() == bookId)
                         ) 
                         .toList(); 
                    }

public BookCopy updateStatus(int id, String status) { 
    BookCopy copy = getById(id); 
    
    if (copy != null) { 
        copy.setStatus( 
            CopyStatus.valueOf(status) 
        ); 
    } 
    return copy; 
}

public void delete(int id) { 
    copies.removeIf(copy -> copy.getId() == id);
 }

    }

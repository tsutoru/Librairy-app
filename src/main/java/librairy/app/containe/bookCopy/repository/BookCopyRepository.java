package librairy.app.containe.bookCopy.repository;

import java.util.List;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, String> {

  List<BookCopy> findByBookIdAndStatus(String bookId, CopyStatus status);

  List<BookCopy> findByStatus(CopyStatus status);

  List<BookCopy> findByBookId(String bookId);
}

package librairy.app.containe.bookCopy.repository;

import librairy.app.containe.bookCopy.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {}

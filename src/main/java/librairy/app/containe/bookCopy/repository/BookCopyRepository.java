package librairy.app.containe.bookCopy.repository;

import librairy.app.containe.bookCopy.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {}

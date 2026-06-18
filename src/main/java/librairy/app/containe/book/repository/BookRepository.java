package librairy.app.containe.book.repository;

import librairy.app.containe.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {}

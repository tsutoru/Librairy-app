package librairy.app.containe.bookCopy.repository; 

import org.springframework.data.jpa.repository.JpaRepository; 
import librairy.app.containe.bookCopy.entity.BookCopy; 

public interface BookCopyRepository 
extends JpaRepository<BookCopy, Integer> {

 }
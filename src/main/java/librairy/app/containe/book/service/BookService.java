package librairy.app.containe.book.service;

import java.util.List;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  @Autowired private BookRepository bookRepository;

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Book create(Book book) {
    return bookRepository.save(book);
  }

  public Book getBookById(String id) {
    return bookRepository.findById((id)).orElse(null);
  }

  public Book update(String id, Book newBook) {
    Book book = getBookById(id);
    if (book != null) {
      book.setTitle(newBook.getTitle());
      book.setDescription(newBook.getDescription());
      book.setPrice(newBook.getPrice());
      book.setPublicationDate(newBook.getPublicationDate());
      book.setIsbn(newBook.getIsbn());
      book.setCategory(newBook.getCategory());
      book.setAuthors(newBook.getAuthors());
      return bookRepository.save(book);
    }
    return null;
  }

  public List<Book> search(String title, String author, String category, String isbn) {
    return bookRepository.findAll().stream()
        .filter(
            book ->
                (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    && (isbn == null || book.getIsbn().toLowerCase().contains(isbn.toLowerCase())))
        .toList();
  }

  public void delete(String id) {
    bookRepository.deleteById((id));
  }
}

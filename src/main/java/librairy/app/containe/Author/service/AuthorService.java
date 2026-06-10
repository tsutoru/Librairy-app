package librairy.app.containe.Author.service;

import java.util.List;
import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.Author.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public Author create(Author author) {
    return authorRepository.save(author);
  }

  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  public Author getById(String id) {
    return authorRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Author not found"));
  }

  public List<Author> getByLastName(String lastName) {
    return authorRepository.findByLastName(lastName);
  }

  public Author update(String id, Author newAuthor) {
    Author author = getById(id);
    author.setFirstName(newAuthor.getFirstName());
    author.setLastName(newAuthor.getLastName());
    author.setBiography(newAuthor.getBiography());
    author.setNationality(newAuthor.getNationality());
    return authorRepository.save(author);
  }

  public void delete(String id) {
    authorRepository.deleteById(id);
  }
}

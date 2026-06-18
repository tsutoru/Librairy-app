package librairy.app.containe.Author.service;

import java.util.List;
import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.Author.repository.AuthorRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;

  public Author create(Author author) {
    if (author.getFirstName() == null || author.getFirstName().isBlank()) {
      throw new BadRequestException("First name is required");
    }
    if (author.getLastName() == null || author.getLastName().isBlank()) {
      throw new BadRequestException("Last name is required");
    }
    return authorRepository.save(author);
  }

  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  public Author getById(String id) {
    return authorRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Author" + id));
  }

  public List<Author> getByLastName(String lastName) {
    return authorRepository.findByLastName(lastName);
  }

  public Author update(String id, Author updated) {
    Author existing = getById(id);

    if (updated.getFirstName() != null && !updated.getFirstName().isBlank()) {
      existing.setFirstName(updated.getFirstName());
    }
    if (updated.getLastName() != null && !updated.getLastName().isBlank()) {
      existing.setLastName(updated.getLastName());
    }
    if (updated.getBiography() != null) {
      existing.setBiography(updated.getBiography());
    }
    if (updated.getNationality() != null) {
      existing.setNationality(updated.getNationality());
    }

    return authorRepository.save(existing);
  }

  public void delete(String id) {
    if (!authorRepository.existsById(id)) {
      throw new ResourceNotFoundException("Author" + id);
    }
    authorRepository.deleteById(id);
  }
}

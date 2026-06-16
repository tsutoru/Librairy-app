package librairy.app.containe.Author.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.Author.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

  @Mock private AuthorRepository authorRepository;

  @InjectMocks private AuthorService authorService;

  private Author author;

  @BeforeEach
  void setUp() {
    author = new Author();
    author.setId("author-1");
    author.setFirstName("John");
    author.setLastName("Doe");
    author.setBiography("Famous writer");
    author.setNationality("French");
  }

  @Test
  void create_shouldSaveAndReturnAuthor() {
    when(authorRepository.save(any(Author.class))).thenReturn(author);

    Author result = authorService.create(author);

    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    verify(authorRepository, times(1)).save(author);
  }

  @Test
  void getAll_shouldReturnListOfAuthors() {
    when(authorRepository.findAll()).thenReturn(List.of(author));

    List<Author> result = authorService.getAll();

    assertEquals(1, result.size());
  }

  @Test
  void getById_shouldReturnAuthor_whenExists() {
    when(authorRepository.findById("author-1")).thenReturn(Optional.of(author));

    Author result = authorService.getById("author-1");

    assertNotNull(result);
    assertEquals("author-1", result.getId());
  }

  @Test
  void getById_shouldThrowException_whenNotFound() {
    when(authorRepository.findById("unknown")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> authorService.getById("unknown"));
  }

  @Test
  void getByLastName_shouldReturnMatchingAuthors() {
    when(authorRepository.findByLastName("Doe")).thenReturn(List.of(author));

    List<Author> result = authorService.getByLastName("Doe");

    assertEquals(1, result.size());
    assertEquals("Doe", result.get(0).getLastName());
  }

  @Test
  void update_shouldModifyAndSaveAuthor() {
    Author updated = new Author();
    updated.setFirstName("Jane");
    updated.setLastName("Smith");
    updated.setBiography("New bio");
    updated.setNationality("American");

    when(authorRepository.findById("author-1")).thenReturn(Optional.of(author));
    when(authorRepository.save(any(Author.class))).thenReturn(author);

    Author result = authorService.update("author-1", updated);

    assertEquals("Jane", result.getFirstName());
    assertEquals("American", result.getNationality());
    verify(authorRepository, times(1)).save(author);
  }

  @Test
  void delete_shouldCallDeleteById() {
    authorService.delete("author-1");

    verify(authorRepository, times(1)).deleteById("author-1");
  }
}

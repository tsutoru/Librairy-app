package librairy.app.containe.Author.controller;

import java.util.List;
import java.util.UUID;

import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.Author.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping
  public ResponseEntity<Author> create(@RequestBody Author author) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(author));
  }

  @GetMapping
  public ResponseEntity<List<Author>> getAll() {
    return ResponseEntity.ok(authorService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Author> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(authorService.getById(id));
  }

  @GetMapping("/search")
  public ResponseEntity<List<Author>> getByLastName(@RequestParam String lastName) {
    return ResponseEntity.ok(authorService.getByLastName(lastName));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Author> update(@PathVariable UUID id, @RequestBody Author author) {
    return ResponseEntity.ok(authorService.update(id, author));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    authorService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

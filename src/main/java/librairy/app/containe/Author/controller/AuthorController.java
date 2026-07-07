package librairy.app.containe.Author.controller;

import java.util.List;
import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.Author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Author create(@RequestBody Author author) {
    return authorService.create(author);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Author> getAll() {
    return authorService.getAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Author getById(@PathVariable String id) {
    return authorService.getById(id);
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public List<Author> getByLastName(@RequestParam String lastName) {
    return authorService.getByLastName(lastName);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Author update(@PathVariable String id, @RequestBody Author author) {
    return authorService.update(id, author);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    authorService.delete(id);
  }
}

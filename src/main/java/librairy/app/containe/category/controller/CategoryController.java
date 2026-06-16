package librairy.app.containe.category.controller;

import java.util.List;
import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Category create(@RequestBody Category category) {
    return categoryService.create(category);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Category> getAll() {
    return categoryService.getAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Category getById(@PathVariable String id) {
    return categoryService.getById(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Category update(@PathVariable String id, @RequestBody Category category) {
    return categoryService.update(id, category);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    categoryService.delete(id);
  }
}

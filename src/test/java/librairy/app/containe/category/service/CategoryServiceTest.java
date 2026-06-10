package librairy.app.containe.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryService categoryService;

  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category();
    category.setId("uuid-1234");
    category.setName("Science Fiction");
    category.setDescription("SF books");
  }

  @Test
  void shouldCreateCategorySuccessfully() {
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    Category result = categoryService.create(category);

    assertNotNull(result);
    assertEquals("Science Fiction", result.getName());
    assertEquals("SF books", result.getDescription());
    verify(categoryRepository, times(1)).save(any(Category.class));
  }

  @Test
  void shouldReturnAllCategories() {
    when(categoryRepository.findAll()).thenReturn(List.of(category));

    List<Category> result = categoryService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Science Fiction", result.get(0).getName());
    verify(categoryRepository, times(1)).findAll();
  }

  @Test
  void shouldReturnCategoryById() {
    when(categoryRepository.findById("uuid-1234")).thenReturn(Optional.of(category));

    Category result = categoryService.getById("uuid-1234");

    assertNotNull(result);
    assertEquals("uuid-1234", result.getId());
    assertEquals("Science Fiction", result.getName());
    verify(categoryRepository, times(1)).findById("uuid-1234");
  }

  @Test
  void shouldThrowExceptionWhenCategoryNotFoundOnGetById() {
    when(categoryRepository.findById("bad-id")).thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> categoryService.getById("bad-id"));

    assertEquals("Category not found with id: bad-id", exception.getMessage());
    verify(categoryRepository, times(1)).findById("bad-id");
  }

  @Test
  void shouldUpdateCategorySuccessfully() {
    Category updated = new Category();
    updated.setName("Fantasy");
    updated.setDescription("Fantasy books");

    when(categoryRepository.findById("uuid-1234")).thenReturn(Optional.of(category));
    when(categoryRepository.save(any(Category.class))).thenReturn(updated);

    Category result = categoryService.update("uuid-1234", updated);

    assertNotNull(result);
    assertEquals("Fantasy", result.getName());
    assertEquals("Fantasy books", result.getDescription());
    verify(categoryRepository, times(1)).findById("uuid-1234");
    verify(categoryRepository, times(1)).save(any(Category.class));
  }

  @Test
  void shouldThrowExceptionWhenCategoryNotFoundOnUpdate() {
    when(categoryRepository.findById("bad-id")).thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> categoryService.update("bad-id", category));

    assertEquals("Category not found with id: bad-id", exception.getMessage());
    verify(categoryRepository, times(1)).findById("bad-id");
  }

  @Test
  void shouldDeleteCategorySuccessfully() {
    when(categoryRepository.existsById("uuid-1234")).thenReturn(true);
    doNothing().when(categoryRepository).deleteById("uuid-1234");

    categoryService.delete("uuid-1234");

    verify(categoryRepository, times(1)).existsById("uuid-1234");
    verify(categoryRepository, times(1)).deleteById("uuid-1234");
  }

  @Test
  void shouldThrowExceptionWhenCategoryNotFoundOnDelete() {
    when(categoryRepository.existsById("bad-id")).thenReturn(false);

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> categoryService.delete("bad-id"));

    assertEquals("Category not found with id: bad-id", exception.getMessage());
    verify(categoryRepository, times(1)).existsById("bad-id");
    verify(categoryRepository, never()).deleteById(any());
  }
}

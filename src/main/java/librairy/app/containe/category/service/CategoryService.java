package librairy.app.containe.category.service;

import java.util.List;
import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.repository.CategoryRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public Category create(Category category) {
    if (category.getName() == null || category.getName().isBlank()) {
      throw new BadRequestException("Category name is required");
    }
    return categoryRepository.save(category);
  }

  public List<Category> getAll() {
    return categoryRepository.findAll();
  }

  public Category getById(String id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category" + id));
  }

  public Category update(String id, Category updated) {
    Category existing = getById(id);

    if (updated.getName() != null && !updated.getName().isBlank()) {
      existing.setName(updated.getName());
    }
    if (updated.getDescription() != null) {
      existing.setDescription(updated.getDescription());
    }

    return categoryRepository.save(existing);
  }

  public void delete(String id) {
    if (!categoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Category" + id);
    }
    categoryRepository.deleteById(id);
  }
}

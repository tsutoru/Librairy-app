package librairy.app.containe.category.service;


import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + id
                ));
    }

    public Category update(String id, Category updatedCategory) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + id
                ));

        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(existing);
    }

    public void delete(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}

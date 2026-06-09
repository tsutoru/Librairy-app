package librairy.app.containe.category.repository;

import librairy.app.containe.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
  boolean existsByNameIgnoreCase(String name);
}

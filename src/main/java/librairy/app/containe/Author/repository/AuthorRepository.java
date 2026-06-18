package librairy.app.containe.Author.repository;

import java.util.List;
import java.util.UUID;

import librairy.app.containe.Author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

  List<Author> findByLastName(String lastName);

  List<Author> findByNationality(String nationality);
}

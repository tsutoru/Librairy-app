package librairy.app.containe.repository;

import java.util.List;
import librairy.app.containe.PojaGenerated;
import librairy.app.containe.repository.model.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@PojaGenerated
@Repository
public interface DummyRepository extends JpaRepository<Dummy, String> {

  @Override
  List<Dummy> findAll();
}

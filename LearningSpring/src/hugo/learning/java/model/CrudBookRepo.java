package hugo.learning.java.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudBookRepo extends CrudRepository<Book, String> {
}

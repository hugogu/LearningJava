package hugo.learning.java.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Repository
public interface CrudBookRepo extends CrudRepository<Book, String> {
    @Override
    @NotNull
    Book save(@Valid @NotNull Book book);
}

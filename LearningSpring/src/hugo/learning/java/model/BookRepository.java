package hugo.learning.java.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface BookRepository {
    Book getByIsbn(@NotNull String isbn);
    double sqrt(double value);
    double sqrt2(double value);
}
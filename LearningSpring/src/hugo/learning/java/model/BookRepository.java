package hugo.learning.java.model;

public interface BookRepository {
    Book getByIsbn(String isbn);
}
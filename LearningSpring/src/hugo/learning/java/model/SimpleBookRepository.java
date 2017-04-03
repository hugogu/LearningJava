package hugo.learning.java.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleBookRepository implements BookRepository {
    private static Logger LOG = LoggerFactory.getLogger(SimpleBookRepository.class);

    public SimpleBookRepository() {
        LOG.info("Creating {}", getClass());
    }

    // This uses Spring annotation, JSR-107 defined another set of annotations for JCache.
    // https://spring.io/blog/2014/04/14/cache-abstraction-jcache-jsr-107-annotations-support
    @Override
    @Cacheable("books")
    public Book getByIsbn(String isbn) {
        simulateSlowService();
        return new Book(isbn, "Some book");
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
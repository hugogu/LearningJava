package hugo.learning.java.spring;

import hugo.learning.java.model.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public abstract class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    @Lookup
    public abstract BookRepository getBookRepository();

    @Override
    public void run(String... args) throws Exception {
        logger.info(".... Fetching books");
        logger.info("isbn-1234 -->" + getBookRepository().getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + getBookRepository().getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + getBookRepository().getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + getBookRepository().getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + getBookRepository().getByIsbn("isbn-1234"));
        logger.info("isbn-1234 -->" + getBookRepository().getByIsbn("isbn-1234"));
    }

}
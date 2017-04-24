package hugo.learning.java.spring;

import hugo.learning.java.model.Book;
import hugo.learning.java.model.BookRepository;
import hugo.learning.java.model.CrudBookRepo;
import hugo.learning.java.services.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public abstract class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    @Lookup
    public abstract BookRepository getBookRepository();

    @Lookup
    public abstract Calculator getCalculator();

    @Override
    public void run(String... args) throws Exception {
        final BookRepository repo = getBookRepository();
        logger.info("2.01 --> " + repo.sqrt(2.01));
        logger.info("2.01 --> " + repo.sqrt(2.01));
        logger.info("2.02 --> " + repo.sqrt(2.02));
        logger.info("2.03 --> " + repo.sqrt(2.03));
        logger.info("2.03 --> " + repo.sqrt2(2.03));
        logger.info("3.00 --> " + repo.sqrt(3.00));
        logger.info("4.004 --> " + getCalculator().sqrt3(4.004));
        logger.info("4.002 --> " + getCalculator().sqrt3(4.002));


        logger.info(".... Fetching books");
        logger.info("isbn-1234 -->" + repo.getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + repo.getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + repo.getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + repo.getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + repo.getByIsbn("isbn-1234"));
        logger.info("isbn-1234 -->" + repo.getByIsbn("isbn-1234"));
    }

    @Bean
    public CommandLineRunner runCrudBookRepo(CrudBookRepo repo) {
        return (args) -> {
            repo.save(new Book("0", "Hugo", BigDecimal.valueOf(0.1)));
        };
    }
}
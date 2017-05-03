package hugo.learning.java.model;


import hugo.learning.java.test.SpringBootTestConfiguration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootTestConfiguration.class })
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepo;

    @Test
    public void cacheTest() {
        Assert.assertEquals(bookRepo.sqrt(2.0), bookRepo.sqrt(2.001), 0.0);
    }
}

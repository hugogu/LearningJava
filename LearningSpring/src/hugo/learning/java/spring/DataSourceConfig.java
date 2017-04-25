package hugo.learning.java.spring;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");

    @Bean
    @Primary
    public DataSource createMemoryDataSource() {
        final String schemaFile = getClass().getResource("/schema.sql").getPath().substring(1);
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;" +
                "INIT=RUNSCRIPT FROM '" + schemaFile +"'\\;");

        return ds;
    }

    @Bean
    @Lazy
    public DataSource createFileDataSource() {
        final String schemaFile = getClass().getResource("/schema.sql").getPath().substring(1);
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:" + TEMP_DIRECTORY + "/testdb3;" +
                  "INIT=RUNSCRIPT FROM '" + schemaFile +"'\\;");

        return ds;
    }
}

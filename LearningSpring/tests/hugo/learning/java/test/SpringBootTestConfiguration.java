package hugo.learning.java.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

@EntityScan(basePackages = {
        "hugo.learning.java.model"
})
@EnableCaching
@SpringBootApplication(scanBasePackages = {
        "hugo.learning.java.model"
})
public class SpringBootTestConfiguration {
    @Bean
    public JCacheManagerCustomizer getCacheManagerCustomizer() {
        return cacheManager -> {
            final MutableConfiguration<Object, Object> configuration =
                    new MutableConfiguration<>()
                            .setTypes(Object.class, Object.class)
                            .setStoreByValue(false)
                            .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
            cacheManager.createCache("math", configuration);
        };
    }
}

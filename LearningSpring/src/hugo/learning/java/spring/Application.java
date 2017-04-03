package hugo.learning.java.spring;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
        "hugo.learning.java.*"
})
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JCacheManagerCustomizer getCacheManagerCustomizer() {
        // Spring only support Object->Object cache.
        //     at org.springframework.cache.jcache.JCacheCacheManager.loadCaches(JCacheCacheManager.java:105)
        //    https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html
        // ehcache can support specific type caching.
        //    http://www.ehcache.org/documentation/3.1/107.html
        return cacheManager -> {
            final MutableConfiguration<Object, Object> configuration =
                    new MutableConfiguration<>()
                            .setTypes(Object.class, Object.class)
                            .setStoreByValue(false)
                            .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
            cacheManager.createCache("books", configuration);
        };
    }
}
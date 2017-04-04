package hugo.learning.java.spring;

import javax.cache.Cache;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;

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
            final Cache cache = cacheManager.createCache("books", configuration);

            // Set entry limitation through ehcache APIs. JSR-107 doesn't seem to support it.
            final Eh107Configuration<Object, Object> eh107Configuration = (Eh107Configuration<Object, Object>) cache.getConfiguration(Eh107Configuration.class);
            final CacheRuntimeConfiguration<Object, Object> runtimeConfiguration = eh107Configuration.unwrap(CacheRuntimeConfiguration.class);
            final ResourcePools pools = ResourcePoolsBuilder
                    .newResourcePoolsBuilder()
                    .heap(20L, EntryUnit.ENTRIES)
                    .build();
            runtimeConfiguration.updateResourcePools(pools);
        };
    }
}
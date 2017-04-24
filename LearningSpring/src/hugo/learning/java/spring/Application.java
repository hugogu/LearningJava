package hugo.learning.java.spring;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import com.google.common.collect.Lists;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackages = {
        "hugo.learning.java.*"
})
@EnableCaching
@EnableAspectJAutoProxy
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CacheManager getCachemanager() throws URISyntaxException {
        final CachingProvider cachingProvider = Caching.getCachingProvider();

        return cachingProvider.getCacheManager(getClass().getResource("/ehcache.xml").toURI(), getClass().getClassLoader());
    }

    // When getCachemanager is used, this getCacheManagerCustomizer will not be called.
    @Bean
    public JCacheManagerCustomizer getCacheManagerCustomizer() {
        // Spring only support Object->Object cache.
        //     at org.springframework.cache.jcache.JCacheCacheManager.loadCaches(JCacheCacheManager.java:105)
        //    https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html
        // ehcache can support specific type caching.
        //    http://www.ehcache.org/documentation/3.1/107.html
        return cacheManager -> {
            final Cache cache = createCacheViaJCacheAPI(cacheManager, Object.class, Object.class);
            configEhCacheViaCode(cache);
        };
    }

    private <K, V> Cache<K, V> createCacheViaJCacheAPI(final CacheManager manager, Class<K> keyClass, Class<V> valueClass) {
        final MutableConfiguration<K, V> configuration =
                new MutableConfiguration<K, V>()
                        .setTypes(keyClass, valueClass)
                        .setStoreByValue(false)
                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

        final Cache<K, V> existingCache = manager.getCache("books", keyClass, valueClass);
        if (existingCache == null) {
            return manager.createCache("books", configuration);
        } else {
            return existingCache;
        }
    }

    private <K, V> void configEhCacheViaCode(final Cache<K, V> cache) {
        // Set entry limitation through ehcache APIs. JSR-107 doesn't seem to support it.
        final Eh107Configuration<K, V> eh107Configuration = (Eh107Configuration<K, V>) cache.getConfiguration(Eh107Configuration.class);
        final CacheRuntimeConfiguration<K, V> runtimeConfiguration = eh107Configuration.unwrap(CacheRuntimeConfiguration.class);
        final ResourcePools pools = ResourcePoolsBuilder
                .newResourcePoolsBuilder()
                .heap(20L, EntryUnit.ENTRIES)
                .build();
        runtimeConfiguration.updateResourcePools(pools);
    }
}
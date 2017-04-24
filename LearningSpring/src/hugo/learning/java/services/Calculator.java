package hugo.learning.java.services;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Calculator {
    private static final Logger LOG = LoggerFactory.getLogger(Calculator.class);
    private Map<Double, Double> rootMap = new ConcurrentHashMap<>();
    private Map<Double, Double> rootLinkedMap = Collections.synchronizedMap(new LinkedHashMap<>());
    private Map<Double, Double> lruMap = Collections.synchronizedMap(new LRUMap<>(10000));

    public double sqrt(double n) {
        return Math.sqrt(n);
    }

    public double sqrt2(double n) {
        return rootMap.computeIfAbsent(n, Math::sqrt);
    }

    public double sqrt2_1(double n) {
        try {
            return rootLinkedMap.computeIfAbsent(n, Math::sqrt);
        } finally {
            if (rootLinkedMap.size() > 1000) {
                rootLinkedMap.remove(rootLinkedMap.keySet().iterator().next());
            }
        }
    }

    public double sqrt2_2(double n) {
        final double r = Precision.round(n, 2);
        try {
            return rootLinkedMap.computeIfAbsent(r, Math::sqrt);
        } finally {
            if (rootLinkedMap.size() > 1000) {
                rootLinkedMap.remove(rootLinkedMap.keySet().iterator().next());
            }
        }
    }

    public static Double round(Number src, int decimalPlaces) {
        return Optional.ofNullable(src)
                       .map(Number::doubleValue)
                       .map(BigDecimal::new)
                       .map(dbl -> dbl.setScale(decimalPlaces))
                       .map(BigDecimal::doubleValue)
                       .orElse(null);
    }

    @Cacheable(value = "math", key = "T(org.apache.commons.math3.util.Precision).round(#n, 2)")
    public double sqrt3(double n) {
        LOG.info("Called sqrt3");
        return Math.sqrt(n);
    }

    @CacheResult(
            cacheName = "math",
            cachedExceptions = { IllegalArgumentException.class })
    public double sqrt4(@CacheKey final double n) {
        return Math.sqrt(n);
    }

    public double roundedSqrt(final double n) {
        return sqrt3(Precision.round(n, 2));
    }
}

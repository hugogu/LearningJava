package hugo.learning.java.model;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.math3.util.Precision;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class StoreMethodResult {
    private Map<Double, Double> lruMap = new LRUMap<>(10000);

    @Around("execution(* hugo.learning.java.*.*(..)) && args(value,..)")
    private Object round(ProceedingJoinPoint pjp, double value) throws Throwable {
        final double r = Precision.round(value, 2);
        return lruMap.computeIfAbsent(r, v -> {
            try {
                return (Double) pjp.proceed(new Object[]{v});
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}

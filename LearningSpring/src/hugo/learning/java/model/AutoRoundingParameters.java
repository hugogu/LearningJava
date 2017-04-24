package hugo.learning.java.model;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AutoRoundingParameters {
    // https://docs.spring.io/spring/docs/current/spring-framework-reference/html/aop.html
    @Around("execution(* hugo.learning.java.*.*(..)) && args(value,..)")
    private Object round(ProceedingJoinPoint pjp, double value) throws Throwable {
        return pjp.proceed(new Object[] { (double) Math.round(value) });
    }
}

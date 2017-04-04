package hugo.learning.java.model;

import static java.util.stream.Collectors.toList;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.stream.Stream;

@Component
public class BookKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return String.join(",", Stream.of(params)
                                      .map(Object::toString)
                                      .collect(toList()));
    }
}

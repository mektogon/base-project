package ru.dorofeev.security.introspector.encrypted.converter;

import org.springframework.stereotype.Component;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ValueConverterResolver {

    private static final Map<Class<?>, ValueConverter<?>> CONVERTER_BY_CLASS = new ConcurrentHashMap<>();

    public ValueConverterResolver(List<ValueConverter<?>> valueConverters) {
        valueConverters.forEach(valueConverter ->
                CONVERTER_BY_CLASS.putIfAbsent(valueConverter.getType(), valueConverter)
        );
    }

    @SuppressWarnings("unchecked")
    public <T> ValueConverter<T> getConverter(Class<T> clazz) {
        ValueConverter<?> valueConverter = CONVERTER_BY_CLASS.get(clazz);

        if (Objects.isNull(valueConverter)) {
            throw new SecurityException(ErrorType.CRYPT_CONVERTER_NOT_FOUND);
        }

        return (ValueConverter<T>) valueConverter;
    }
}

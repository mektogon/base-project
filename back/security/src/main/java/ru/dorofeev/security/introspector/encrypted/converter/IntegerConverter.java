package ru.dorofeev.security.introspector.encrypted.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Класс-преобразователь в тип {@link Integer}.
 */
@Component
public class IntegerConverter implements ValueConverter<Integer> {

    @Override
    public String convertToString(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return String.valueOf(value);
    }

    @Override
    public Integer convertTo(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}

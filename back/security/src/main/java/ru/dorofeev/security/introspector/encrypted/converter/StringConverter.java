package ru.dorofeev.security.introspector.encrypted.converter;

import org.springframework.stereotype.Component;

/**
 * Класс-преобразователь в тип {@link String}.
 */
@Component
public class StringConverter implements ValueConverter<String> {

    @Override
    public String convertToString(String value) {
        return value;
    }

    @Override
    public String convertTo(String value) {
        return value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}

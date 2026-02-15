package ru.dorofeev.security.introspector.encrypted.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * Класс-преобразователь в тип {@link UUID}.
 */
@Component
public class UuidConverter implements ValueConverter<UUID> {

    @Override
    public String convertToString(UUID value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return String.valueOf(value);
    }

    @Override
    public UUID convertTo(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return UUID.fromString(value);
    }

    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }
}

package ru.dorofeev.security.session.section;

import org.springframework.stereotype.Component;
import ru.dorofeev.security.session.utils.SessionUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class CryptSection implements AbstractSection<CryptSection.Attribute> {

    private static final String ALGORITHM_VALUE = "AES/GCM/NoPadding";

    @Override
    public String getName() {
        return "CRYPT";
    }

    @Override
    public String getDescription() {
        return "Информация о шифровании";
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put(Attribute.SECRET_KEY.name(), SessionUtils.getSecretKey());
        attributes.put(Attribute.ALGORITHM.name(), ALGORITHM_VALUE);

        return attributes;
    }

    @Override
    public Class<Attribute> getAttributeType() {
        return Attribute.class;
    }

    public enum Attribute {

        /**
         * Ключ для шифрования/дешифрования объектов. <br/>
         * Тип: {@link String}.
         */
        SECRET_KEY,

        /**
         * Алгоритм шифрования. <br/>
         * Тип: {@link String}.
         */
        ALGORITHM;
    }
}

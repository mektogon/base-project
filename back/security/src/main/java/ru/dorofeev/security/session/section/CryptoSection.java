package ru.dorofeev.security.session.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.introspector.encrypted.provider.CryptoProvider;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CryptoSection implements AbstractSection<CryptoSection.Attribute> {

    @Override
    public String getName() {
        return "CRYPTO";
    }

    @Override
    public String getDescription() {
        return "Криптографическая информация";
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put(Attribute.SECRET_KEY.name(), CryptoProvider.generate32ByteSecretKey());
        attributes.put(Attribute.ALGORITHM.name(), CryptoProvider.getAlgorithm());
        attributes.put(Attribute.TRANSFORMATION.name(), CryptoProvider.getAlgorithmTransformation());

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
        ALGORITHM,

        /**
         * Трансформация шифрования.
         * Тип: {@link String}.
         */
        TRANSFORMATION;
    }
}

package ru.dorofeev.security.session.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.introspector.encrypted.provider.CryptoProvider;
import ru.dorofeev.security.session.service.SessionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CryptoSection implements AbstractSection<CryptoSection.Attribute> {

    private final SessionService service;

    @Override
    public String getName() {
        return "CRYPTO";
    }

    @Override
    public String getDescription() {
        return "Криптографическая информация";
    }

    @Override
    public Map<String, Object> initialize() {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put(Attribute.SECRET_KEY.getName(), CryptoProvider.generate32ByteSecretKey());
        attributes.put(Attribute.ALGORITHM.getName(), CryptoProvider.getAlgorithm());
        attributes.put(Attribute.TRANSFORMATION.getName(), CryptoProvider.getAlgorithmTransformation());

        return attributes;
    }

    @Override
    public List<Attribute> getAttributes() {
        return List.of(Attribute.values());
    }

    public enum Attribute implements AbstractAttributeSection<CryptoSection> {

        /**
         * Ключ для шифрования/дешифрования объектов. <br/>
         * Тип: {@link String}.
         */
        SECRET_KEY {
            @Override
            public String getName() {
                return SECRET_KEY.name();
            }

            @Override
            public Object getValue(CryptoSection section) {
                return section.getSecretKey();
            }
        },

        /**
         * Алгоритм шифрования. <br/>
         * Тип: {@link String}.
         */
        ALGORITHM {
            @Override
            public String getName() {
                return ALGORITHM.name();
            }

            @Override
            public Object getValue(CryptoSection section) {
                return section.getAlgorithm();
            }
        },

        /**
         * Трансформация шифрования.
         * Тип: {@link String}.
         */
        TRANSFORMATION {
            @Override
            public String getName() {
                return TRANSFORMATION.getName();
            }

            @Override
            public Object getValue(CryptoSection section) {
                return section.getTransformation();
            }
        },

        ;
    }

    private Object getSecretKey() {
        return service.getSectionAttribute(this, Attribute.SECRET_KEY);
    }

    private Object getAlgorithm() {
        return service.getSectionAttribute(this, Attribute.ALGORITHM);
    }

    private Object getTransformation() {
        return service.getSectionAttribute(this, Attribute.TRANSFORMATION);
    }
}

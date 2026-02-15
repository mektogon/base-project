package ru.dorofeev.security.introspector;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.introspector.base64decode.Base64StringDecode;
import ru.dorofeev.security.introspector.base64decode.Base64StringDecodeDeserializer;
import ru.dorofeev.security.introspector.encrypted.Encrypted;
import ru.dorofeev.security.introspector.encrypted.EncryptedDeserializer;
import ru.dorofeev.security.introspector.encrypted.EncryptedSerializer;
import ru.dorofeev.security.introspector.encrypted.converter.ValueConverterResolver;
import ru.dorofeev.security.introspector.encrypted.provider.CryptoProvider;

/**
 * Интроспектор пользовательских аннотаций при сериализации/десериализации. <br/>
 * Обработка аннотаций:
 * <li>{@link Base64StringDecode}</li>
 * <li>{@link Encrypted}</li>
 */
@Component
@RequiredArgsConstructor
public class CommonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private final ValueConverterResolver valueConverterResolver;
    private final CryptoProvider cryptoProvider;

    @Override
    public Object findSerializer(Annotated a) {
        if (a.hasAnnotation(Encrypted.class)) {
            return new EncryptedSerializer<>(a.getRawType(), valueConverterResolver, cryptoProvider);
        }

        return super.findSerializer(a);
    }

    @Override
    public Object findDeserializer(Annotated a) {
        if (a.hasAnnotation(Base64StringDecode.class)) {
            return Base64StringDecodeDeserializer.class;
        }

        if (a.hasAnnotation(Encrypted.class)) {
            return getEncryptedDeserializer(a);
        }

        return super.findDeserializer(a);
    }

    private @NotNull EncryptedDeserializer<Object> getEncryptedDeserializer(Annotated a) {
        JavaType javaType = a.getType();

        if (a instanceof AnnotatedMethod method) {
            if (method.hasAnnotation(Encrypted.class)) {
                javaType = method.getParameterType(0);
            }
        }

        return new EncryptedDeserializer<>(javaType.getRawClass(), valueConverterResolver, cryptoProvider);
    }
}

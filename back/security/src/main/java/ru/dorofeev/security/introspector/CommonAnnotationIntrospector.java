package ru.dorofeev.security.introspector;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.introspector.base64decode.Base64StringDecode;
import ru.dorofeev.security.introspector.base64decode.Base64StringDecodeDeserializer;

/**
 * Интроспектор пользовательских аннотаций при сериализации/десериализации. <br/>
 * Обработка аннотаций:
 * <li>{@link Base64StringDecode}</li>
 */
@Component
@RequiredArgsConstructor
public class CommonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated a) {
        return super.findSerializer(a);
    }

    @Override
    public Object findDeserializer(Annotated a) {
        if (a.hasAnnotation(Base64StringDecode.class)) {
            return Base64StringDecodeDeserializer.class;
        }
        return super.findDeserializer(a);
    }
}

package ru.dorofeev.security.introspector.base64decode;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64StringDecodeDeserializer extends StdScalarDeserializer<String> {

    protected Base64StringDecodeDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();

        if (StringUtils.isBlank(value)) {
            return null;
        }

        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}

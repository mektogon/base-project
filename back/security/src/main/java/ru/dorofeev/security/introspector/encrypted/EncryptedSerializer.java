package ru.dorofeev.security.introspector.encrypted;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import ru.dorofeev.security.introspector.encrypted.converter.ValueConverterResolver;
import ru.dorofeev.security.introspector.encrypted.provider.CryptoProvider;

import java.io.IOException;

public class EncryptedSerializer<T> extends StdScalarSerializer<T> {

    private final ValueConverterResolver valueConverterResolver;
    private final CryptoProvider cryptoProvider;
    private final Class<T> clazz;

    public EncryptedSerializer(Class<T> t, ValueConverterResolver valueConverterResolver, CryptoProvider cryptoProvider) {
        super(t);
        this.clazz = t;
        this.valueConverterResolver = valueConverterResolver;
        this.cryptoProvider = cryptoProvider;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String valueAsString = valueConverterResolver.getConverter(clazz).convertToString(value);
        gen.writeString(cryptoProvider.encrypt(valueAsString));
    }
}

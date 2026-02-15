package ru.dorofeev.security.introspector.encrypted;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import ru.dorofeev.security.introspector.encrypted.converter.ValueConverterResolver;
import ru.dorofeev.security.introspector.encrypted.provider.CryptoProvider;

import java.io.IOException;

public class EncryptedDeserializer<T> extends StdScalarDeserializer<T> {

    private final ValueConverterResolver valueConverterResolver;
    private final CryptoProvider cryptoProvider;
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public EncryptedDeserializer(Class<?> vc, ValueConverterResolver valueConverterResolver, CryptoProvider cryptoProvider) {
        super(vc);
        this.valueConverterResolver = valueConverterResolver;
        this.cryptoProvider = cryptoProvider;
        this.clazz = (Class<T>) vc;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return valueConverterResolver.getConverter(clazz)
                .convertTo(
                        cryptoProvider.decrypt(
                                p.getValueAsString()
                        )
                );
    }
}

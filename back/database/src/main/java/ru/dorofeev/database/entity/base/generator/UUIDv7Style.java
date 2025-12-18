package ru.dorofeev.database.entity.base.generator;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.uuid.UuidValueGenerator;

import java.util.UUID;

/**
 * Генератор <a href="https://www.ietf.org/rfc/rfc9562.html">UUIDv7</a>.
 */
public class UUIDv7Style implements UuidValueGenerator {

    private static final TimeBasedEpochGenerator GENERATOR = Generators.timeBasedEpochGenerator();

    @Override
    public UUID generateUuid(SharedSessionContractImplementor session) {
        return GENERATOR.generate();
    }

    /**
     * Генерация <a href="https://www.ietf.org/rfc/rfc9562.html">UUIDv7</a>.
     *
     * @return UUIDv7 ({@link com.fasterxml.uuid.impl.TimeBasedEpochGenerator}).
     */
    public static UUID random() {
        return GENERATOR.generate();
    }
}
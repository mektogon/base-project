package ru.dorofeev.database.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import ru.dorofeev.database.entity.base.generator.UUIDv7Style;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BasicEntityFieldsWithIdGeneration extends BasicEntitySystemFields {

    /**
     * Идентификатор записи. (С автогенерацией)
     */
    @Id
    @UuidGenerator(algorithm = UUIDv7Style.class)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
}

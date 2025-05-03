package ru.dorofeev.database.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BasicEntityFieldsWithoutIdGeneration extends BasicEntitySystemFields {

    /**
     * Идентификатор записи. (Без автогенерации)
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
}

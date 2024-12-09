package ru.dorofeev.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.dorofeev.database.entity.base.BasicEntityFieldsWithIdGeneration;

/**
 * Сущность с универсальными рубильниками.
 */
@Table(name = "common_setting")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommonSetting extends BasicEntityFieldsWithIdGeneration {

    /**
     * Код рубильника.
     */
    @Column(name = "code")
    private String code;

    /**
     * Значение рубильника.
     */
    @Column(name = "value")
    private String value;

    /**
     * Описание рубильника.
     */
    @Column(name = "description")
    private String description;
}

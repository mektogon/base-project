package ru.dorofeev.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.dorofeev.database.entity.base.BasicEntityFieldsWithIdGeneration;
import ru.dorofeev.database.entity.enums.JobType;
import ru.dorofeev.database.entity.enums.StatusType;

/**
 * Сущность с запускаемыми задачами.
 */
@Table(name = "job")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Job extends BasicEntityFieldsWithIdGeneration {

    /**
     * Уникальное наименование задачи
     */
    @Column(name = "name")
    private String name;

    /**
     * Описание задачи.
     */
    @Column(name = "description")
    private String description;

    /**
     * Включена ли задача (true\false).
     */
    @Column(name = "enabled")
    private boolean enabled;

    /**
     * Статус задачи работы задачи.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusType status;

    /**
     * Тип задачи.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private JobType type;
}

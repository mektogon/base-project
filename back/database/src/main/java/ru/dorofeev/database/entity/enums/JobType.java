package ru.dorofeev.database.entity.enums;

/**
 * Тип задачи.
 */
public enum JobType {

    /**
     * Планировщик.
     */
    SCHEDULER,

    /**
     * Загрузчик/Проливщик данных.
     */
    LOADER,

    /**
     * Любая другая задача.
     */
    OTHER;
}

package ru.dorofeev.security.session.section;

import java.util.List;
import java.util.Map;

/**
 * Контракт для создания секции.
 */
public interface AbstractSection<T extends Enum<T>> {

    /**
     * Префикс наименования секции. <br/>
     * Пример: SOME_PREFIX_ + SOME_SECTION = SOME_PREFIX_SOME_SECTION
     */
    String SECTION_SESSION_PREFIX = "SECTION_";

    /**
     * Наименование секции.
     */
    String getName();

    /**
     * Описание секции.
     */
    String getDescription();

    /**
     * Инициализация атрибутного состава секции.
     */
    Map<String, Object> initialize();

    /**
     * Получить список атрибутов.
     */
    List<T> getAttributes();
}

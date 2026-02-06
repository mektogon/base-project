package ru.dorofeev.security.session.section;

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
     * Получение атрибутного состава секции.
     */
    Map<String, Object> getAttributes();

    /**
     * Получить enum класс атрибутов.
     */
    Class<T> getAttributeType();
}

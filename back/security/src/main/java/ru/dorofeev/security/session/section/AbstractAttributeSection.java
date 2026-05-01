package ru.dorofeev.security.session.section;

public interface AbstractAttributeSection<T extends AbstractSection<?>> {

    /**
     * Наименование параметра в секции.
     */
    String getName();

    /**
     * Получение значения из сессии.
     */
    Object getValue(T section);
}

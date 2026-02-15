package ru.dorofeev.security.introspector.encrypted.converter;

public interface ValueConverter<T> {

    /**
     * Преобразовать значение типа {@link T} в {@link String}.
     *
     * @param value преобразуемое значение.
     * @return значение типа {@link T} преобразованное в {@link String}.
     */
    String convertToString(T value);

    /**
     * Преобразовать значение типа {@link String} в {@link T}.
     *
     * @param value преобразуемое значение.
     * @return значение типа {@link String} преобразованное в {@link T}.
     */
    T convertTo(String value);

    /**
     * Получить тип класса.
     */
    Class<T> getType();
}

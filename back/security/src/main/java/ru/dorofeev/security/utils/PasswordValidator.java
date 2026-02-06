package ru.dorofeev.security.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Класс для валидации пароля.
 */
public class PasswordValidator {

    /**
     * Метод валидации пароля.
     * Валидация по условиям:
     * <li/> Соответствие допустимым символам.
     * <li/> Наличие одной заглавной буквы.
     * <li/> Наличие одной прописной буквы.
     * <li/> Наличие одной цифры.
     * <li/> Соответствие разрешенной длине.
     *
     * @param password             пароль пользователя.
     * @param minLength            минимально разрешенная длина пароля.
     * @param maxLength            максимально разрешенная длина пароля.
     * @param regExpAllowedSymbols регулярное выражение с допустимыми символами.
     * @return объект типа {@link Pair}, ключ которого выражает валидность пароля.
     * Если ключ равен false, то значение объекта {@link Pair} будет содержать сообщение, что пошло не так.
     * Если ключ равен true, то значение объекта {@link Pair} равно null.
     */
    public Pair<Boolean, String> passwordIsValid(
            String password,
            Integer minLength,
            Integer maxLength,
            String regExpAllowedSymbols
    ) {

        if (StringUtils.isBlank(password)) {
            return Pair.of(false, buildSizeMessage(0, minLength, maxLength));
        }

        if (!password.matches(regExpAllowedSymbols)) {
            return Pair.of(
                    false,
                    "Пароль может содержать только: английские буквы (A-Z, a-z), цифры (0-9) и символы !@#$%^&*()_+-=[]{}|:,.<>?/`~"
            );
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }

            if (Character.isLowerCase(c)) {
                hasLower = true;
            }

            if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasUpper && hasLower && hasDigit) {
                break;
            }
        }

        if (!hasUpper) {
            return Pair.of(
                    false,
                    "Пароль должен содержать хотя бы одну заглавную букву!"
            );
        }

        if (!hasLower) {
            return Pair.of(
                    false,
                    "Пароль должен содержать хотя бы одну строчную букву!"
            );
        }

        if (!hasDigit) {
            return Pair.of(
                    false,
                    "Пароль должен содержать хотя бы одну цифру!"
            );
        }

        int length = password.length();
        if (!(length >= minLength && length <= maxLength)) {
            return Pair.of(false, buildSizeMessage(length, minLength, maxLength));
        } else {
            return Pair.of(true, null);
        }
    }

    /**
     * Метод формирования сообщения о допустимой длине пароля.
     * Если max == {@link Integer#MAX_VALUE}, то максимальная длина не указывается.
     * В ином случае выводятся обе границы.
     */
    private String buildSizeMessage(int currentLength, Integer minLength, Integer maxLength) {
        if (Integer.MAX_VALUE == maxLength) {
            return "Пароль должен содержать от %s символов! Текущая длина: %s"
                    .formatted(minLength, currentLength);
        } else if (currentLength == 0) {
            return "Пароль должен содержать от %s символов!".formatted(minLength);
        } else {
            return "Пароль должен содержать от %s до %s символов включительно! Текущая длина: %s"
                    .formatted(minLength, maxLength, currentLength);
        }
    }
}

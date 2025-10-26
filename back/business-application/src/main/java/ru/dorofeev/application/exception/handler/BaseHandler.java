package ru.dorofeev.application.exception.handler;

import ru.dorofeev.application.exception.model.ErrorData;

public class BaseHandler {

    protected static final String EMPTY_ERROR_FIELD = null;

    /**
     * Метод формирования объекта типа {@link ErrorData}.
     *
     * @param errorField   наименование параметра, в котором произошла ошибка.
     * @param errorMessage сообщение с ошибкой, выводимое на UI.
     * @return объект типа {@link ErrorData}.
     */
    protected ErrorData buildErrorData(String errorField, String errorMessage) {
        return ErrorData.builder()
                .errorField(errorField)
                .errorMessage(errorMessage)
                .build();
    }
}

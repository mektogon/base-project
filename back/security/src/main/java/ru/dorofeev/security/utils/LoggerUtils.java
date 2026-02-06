package ru.dorofeev.security.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class LoggerUtils {

    /**
     * Метод выполняет ленивый вывод сообщения с уровнем DEBUG.
     *
     * @param messageSupplier выводимое сообщение.
     */
    public static void debugLazy(Supplier<String> messageSupplier) {
        if (!log.isDebugEnabled()) {
            return;
        }

        log.debug(messageSupplier.get());
    }
}

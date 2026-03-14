package ru.dorofeev.notification.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.dorofeev.notification.model.enums.NotificationType;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramNotificationStrategy implements NotificationStrategy {

    @Override
    public boolean support(NotificationType type) {
        return NotificationType.TELEGRAM == type;
    }

    @Override
    public void apply() {
        throw new UnsupportedOperationException("Уведомление типа TELEGRAM не поддерживается!");
    }
}

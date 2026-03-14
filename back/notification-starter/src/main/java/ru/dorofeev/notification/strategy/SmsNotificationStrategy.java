package ru.dorofeev.notification.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.dorofeev.notification.model.enums.NotificationType;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public boolean support(NotificationType type) {
        return NotificationType.SMS == type;
    }

    @Override
    public void apply() {
        throw new UnsupportedOperationException("Уведомление типа SMS не поддерживается!");
    }
}

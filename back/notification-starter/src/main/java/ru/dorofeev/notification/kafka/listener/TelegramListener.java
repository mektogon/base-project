package ru.dorofeev.notification.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.dorofeev.notification.model.NotificationMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramListener implements NotificationListener {

    @KafkaListener(
            id = "telegramNotificationListener",
            concurrency = "${notification.kafka-setting.listener.telegram.concurrency}",
            groupId = "${notification.kafka-setting.listener.telegram.group-id}",
            topics = "${notification.kafka-setting.listener.telegram.topic}"
    )
    @Override
    public void listen(List<NotificationMessage> notifications) {

    }
}

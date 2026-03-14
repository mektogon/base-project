package ru.dorofeev.notification.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.dorofeev.notification.mail.sender.service.EMailSender;
import ru.dorofeev.notification.model.NotificationMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailListener implements NotificationListener {

    private final EMailSender sender;

    @KafkaListener(
            id = "emailNotificationListener",
            concurrency = "${notification.kafka-setting.listener.email.concurrency}",
            groupId = "${notification.kafka-setting.listener.email.group-id}",
            topics = "${notification.kafka-setting.listener.email.topic}"
    )
    @Override
    public void listen(List<NotificationMessage> notifications) {

    }
}

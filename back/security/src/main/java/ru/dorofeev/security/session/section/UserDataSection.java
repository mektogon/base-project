package ru.dorofeev.security.session.section;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dorofeev.database.entity.UserInfoEntity;
import ru.dorofeev.database.repository.UserInfoRepository;
import ru.dorofeev.security.session.utils.SessionUtils;
import ru.dorofeev.security.utils.HttpServletUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDataSection implements AbstractSection<UserDataSection.Attribute> {

    private final UserInfoRepository userInfoRepository;

    @Override
    public String getName() {
        return "USER_DATA";
    }

    @Override
    public String getDescription() {
        return "Информация о пользователе";
    }

    @Override
    public Map<String, Object> getAttributes() {
        HttpServletRequest currentRequest = HttpServletUtils.getCurrentHttpRequest();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(Attribute.IP_ADDRESS.name(), SessionUtils.getIp(currentRequest));
        attributes.put(Attribute.CHANNEL.name(), SessionUtils.getChannelByUserAgent(currentRequest));

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        UserInfoEntity user = userInfoRepository.findByEmail(HttpServletUtils.getCurrentPrincipalName()).get();

        attributes.put(UserDataSection.Attribute.USER_ID.name(), user.getId());
        attributes.put(UserDataSection.Attribute.USER_FIRST_NAME.name(), user.getFirstName());
        attributes.put(UserDataSection.Attribute.USER_MIDDLE_NAME.name(), user.getMiddleName());
        attributes.put(UserDataSection.Attribute.USER_LAST_NAME.name(), user.getLastName());
        attributes.put(UserDataSection.Attribute.USER_PHONE.name(), user.getPhoneNumber());
        attributes.put(UserDataSection.Attribute.USER_EMAIL.name(), user.getEmail());

        return attributes;
    }

    @Override
    public Class<Attribute> getAttributeType() {
        return Attribute.class;
    }

    public enum Attribute {

        /**
         * IP-адрес клиента. <br/>
         * Тип: {@link String}.
         */
        IP_ADDRESS,

        /**
         * Канал клиента. <br/>
         * Пример: WEB/MOBILE. <br/>
         * Тип: {@link String}.
         */
        CHANNEL,

        /**
         * Идентификатор клиента. <br/>
         * Тип: {@link java.util.UUID}.
         */
        USER_ID,

        /**
         * Имя пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_FIRST_NAME,

        /**
         * Отчество пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_MIDDLE_NAME,

        /**
         * Фамилия пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_LAST_NAME,

        /**
         * Телефон пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_PHONE,

        /**
         * E-MAIL пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_EMAIL,

        ;
    }
}

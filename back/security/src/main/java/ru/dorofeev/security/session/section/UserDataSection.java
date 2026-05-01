package ru.dorofeev.security.session.section;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dorofeev.database.entity.UserInfoEntity;
import ru.dorofeev.database.repository.UserInfoRepository;
import ru.dorofeev.security.session.service.SessionService;
import ru.dorofeev.security.session.utils.SessionUtils;
import ru.dorofeev.security.utils.HttpServletUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDataSection implements AbstractSection<UserDataSection.Attribute> {

    private final UserInfoRepository userInfoRepository;
    private final SessionService service;

    @Override
    public String getName() {
        return "USER_DATA";
    }

    @Override
    public String getDescription() {
        return "Информация о пользователе";
    }

    @Override
    public Map<String, Object> initialize() {
        HttpServletRequest currentRequest = HttpServletUtils.getCurrentHttpRequest();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put(Attribute.IP_ADDRESS.getName(), SessionUtils.getIp(currentRequest));
        attributes.put(Attribute.CHANNEL.getName(), SessionUtils.getChannelByUserAgent(currentRequest));

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        UserInfoEntity user = userInfoRepository.findByEmail(HttpServletUtils.getCurrentPrincipalName()).get();

        attributes.put(UserDataSection.Attribute.USER_ID.getName(), user.getId());
        attributes.put(UserDataSection.Attribute.USER_FIRST_NAME.getName(), user.getFirstName());
        attributes.put(UserDataSection.Attribute.USER_MIDDLE_NAME.getName(), user.getMiddleName());
        attributes.put(UserDataSection.Attribute.USER_LAST_NAME.getName(), user.getLastName());
        attributes.put(UserDataSection.Attribute.USER_PHONE.getName(), user.getPhoneNumber());
        attributes.put(UserDataSection.Attribute.USER_EMAIL.getName(), user.getEmail());

        return attributes;
    }

    @Override
    public List<Attribute> getAttributes() {
        return List.of(Attribute.values());
    }

    public enum Attribute implements AbstractAttributeSection<UserDataSection> {

        /**
         * IP-адрес клиента. <br/>
         * Тип: {@link String}.
         */
        IP_ADDRESS {
            @Override
            public String getName() {
                return IP_ADDRESS.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getIpAddress();
            }
        },

        /**
         * Канал клиента. <br/>
         * Пример: WEB/MOBILE. <br/>
         * Тип: {@link String}.
         */
        CHANNEL {
            @Override
            public String getName() {
                return CHANNEL.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getChannel();
            }
        },

        /**
         * Идентификатор клиента. <br/>
         * Тип: {@link java.util.UUID}.
         */
        USER_ID {
            @Override
            public String getName() {
                return USER_ID.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getUserId();
            }
        },

        /**
         * Имя пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_FIRST_NAME {
            @Override
            public String getName() {
                return USER_FIRST_NAME.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getUserFirstName();
            }
        },

        /**
         * Отчество пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_MIDDLE_NAME {
            @Override
            public String getName() {
                return USER_FIRST_NAME.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getUserMiddleName();
            }
        },

        /**
         * Фамилия пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_LAST_NAME {
            @Override
            public String getName() {
                return USER_FIRST_NAME.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getUserLastName();
            }
        },

        /**
         * Телефон пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_PHONE {
            @Override
            public String getName() {
                return USER_FIRST_NAME.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getUserPhone();
            }
        },

        /**
         * E-MAIL пользователя. <br/>
         * Тип: {@link String}.
         */
        USER_EMAIL {
            @Override
            public String getName() {
                return USER_EMAIL.name();
            }

            @Override
            public Object getValue(UserDataSection section) {
                return section.getEmail();
            }
        },

        ;
    }

    private Object getIpAddress() {
        return service.getSectionAttribute(this, Attribute.IP_ADDRESS);
    }

    private Object getChannel() {
        return service.getSectionAttribute(this, Attribute.CHANNEL);
    }

    private Object getUserId() {
        return service.getSectionAttribute(this, Attribute.USER_ID);
    }

    private Object getUserFirstName() {
        return service.getSectionAttribute(this, Attribute.USER_FIRST_NAME);
    }

    private Object getUserMiddleName() {
        return service.getSectionAttribute(this, Attribute.USER_MIDDLE_NAME);
    }

    private Object getUserLastName() {
        return service.getSectionAttribute(this, Attribute.USER_LAST_NAME);
    }

    private Object getUserPhone() {
        return service.getSectionAttribute(this, Attribute.USER_PHONE);
    }

    private Object getEmail() {
        return service.getSectionAttribute(this, Attribute.USER_EMAIL);
    }
}

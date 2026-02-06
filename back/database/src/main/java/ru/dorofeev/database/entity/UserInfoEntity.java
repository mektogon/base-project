package ru.dorofeev.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.dorofeev.database.entity.base.BasicEntityFieldsWithIdGeneration;
import ru.dorofeev.database.entity.enums.UserStatus;
import ru.dorofeev.database.entity.enums.UserType;
import ru.dorofeev.database.entity.enums.VerificationStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Пользовательская информация.
 */
@Table(name = "user_info")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEntity extends BasicEntityFieldsWithIdGeneration {

    /**
     * E-mail пользователя.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Тип аккаунта. (Пользователь/Организация)
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserType type = UserType.USER;

    /**
     * Верификация E-mail.
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false)
    private VerificationStatus emailStatus = VerificationStatus.NOT_VERIFIED;

    /**
     * Хеш пароля.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * Дата истечения пароля.
     */
    @Column(name = "password_expiration_date", nullable = false)
    private LocalDate passwordExpirationDate;

    /**
     * Имя пользователя/Наименование организации.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Отчество пользователя.
     */
    @Column(name = "middle_name")
    private String middleName;

    /**
     * Статус пользователя.
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    /**
     * Номер телефона.
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /**
     * Верификация телефона.
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "phone_status", nullable = false)
    private VerificationStatus phoneStatus = VerificationStatus.NOT_VERIFIED;

    /**
     * Дата и время последнего логина пользователя.
     */
    @Builder.Default
    @Column(name = "last_login_date_time", nullable = false)
    private OffsetDateTime lastLoginDateTime = OffsetDateTime.now();
}

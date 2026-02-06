package ru.dorofeev.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.dorofeev.database.entity.base.BasicEntityFieldsWithIdGeneration;

import java.util.UUID;

/**
 * История паролей пользователя.
 */
@Table(name = "user_password_history")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordHistory extends BasicEntityFieldsWithIdGeneration {

    /**
     * Информация по пользователю.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfoEntity userInfo;

    /**
     * Идентификатор пользователя.
     */
    @Column(name = "user_info_id", nullable = false, insertable = false, updatable = false)
    private UUID userId;

    /**
     * Хеш пароля.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * Алгоритм хеширования пароля.
     */
    @Column(name = "password_hash_algorithm", nullable = false)
    private String passwordHashAlgorithm;
}

package ru.dorofeev.database.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dorofeev.database.entity.UserInfoEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, UUID> {

    @Query("SELECT u FROM UserInfoEntity u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserInfoEntity> findByEmail(String email);

    @Query("SELECT EXISTS(SELECT 1 FROM UserInfoEntity u WHERE LOWER(u.email) = LOWER(:email))")
    boolean existByEmail(String email);

    @Query("SELECT EXISTS(SELECT 1 FROM UserInfoEntity u WHERE u.phoneNumber = :phoneNumber)")
    boolean existByPhoneNumber(String phoneNumber);

    @Query("""
            UPDATE UserInfoEntity u
                SET u.lastLoginDateTime = CURRENT_TIMESTAMP
            WHERE LOWER(u.email) = LOWER(:email)
            """
    )
    @Modifying
    @Transactional
    void updateLastLoginDateTimeByEmail(String email);
}

package ru.dorofeev.database.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dorofeev.database.entity.UserPasswordHistory;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPasswordHistoryRepository extends JpaRepository<UserPasswordHistory, UUID> {

    @Query("""
            SELECT u FROM UserPasswordHistory u
                 WHERE u.userId = :userId
                 ORDER BY u.createDateTime ASC
                 LIMIT :limitCount
            """
    )
    List<UserPasswordHistory> getHistoryPasswordByUserLimit(UUID userId, Integer limitCount);

    @Query(value = """
            DELETE FROM user_password_history
                WHERE id IN (
                    SELECT id FROM user_password_history
                    WHERE user_info_id = :userId
                    ORDER BY create_date_time DESC
                    OFFSET (:skipCount - 1)
                    FOR UPDATE
                )
            """, nativeQuery = true
    )
    @Modifying
    @Transactional
    void deleteOldestPassword(UUID userId, int skipCount);
}

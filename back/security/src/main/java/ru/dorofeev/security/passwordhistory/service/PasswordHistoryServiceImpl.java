package ru.dorofeev.security.passwordhistory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.dorofeev.database.entity.UserPasswordHistory;
import ru.dorofeev.database.repository.UserInfoRepository;
import ru.dorofeev.database.repository.UserPasswordHistoryRepository;
import ru.dorofeev.security.SecurityProperties;
import ru.dorofeev.security.configuration.PepperedBCryptPasswordEncoder;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordHistoryServiceImpl implements PasswordHistoryService {

    private final SecurityProperties securityProperties;

    private final PepperedBCryptPasswordEncoder passwordEncoder;

    private final UserPasswordHistoryRepository userPasswordHistoryRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public void savePasswordHash(@NotNull UUID userId, @NotNull String encodedPassword) {
        userPasswordHistoryRepository.deleteOldestPassword(userId, securityProperties.getPassword().getHistory().getDepth());

        userPasswordHistoryRepository.save(
                UserPasswordHistory.builder()
                        .passwordHash(encodedPassword)
                        .passwordHashAlgorithm(securityProperties.getPassword().getAlgorithm())
                        .userInfo(userInfoRepository.getReferenceById(userId))
                        .build()
        );
    }

    @Override
    public boolean checkRawPassword(@NotNull UUID userId, @NotNull String rawPassword) {
        return userPasswordHistoryRepository.getHistoryPasswordByUserLimit(userId, securityProperties.getPassword().getHistory().getDepth())
                .stream()
                .anyMatch(passwordFromHistory ->
                        passwordEncoder.matches(rawPassword, passwordFromHistory.getPasswordHash())
                );
    }
}

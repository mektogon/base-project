package ru.dorofeev.security.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.dorofeev.database.entity.UserInfoEntity;
import ru.dorofeev.database.entity.enums.UserType;
import ru.dorofeev.database.repository.UserInfoRepository;
import ru.dorofeev.security.SecurityProperties;
import ru.dorofeev.security.authentication.model.SecurityUserDetails;
import ru.dorofeev.security.session.service.SessionService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class SecurityLocalConfiguration {

    private final SecurityProperties securityProperties;
    private final UserInfoRepository userInfoRepository;
    private final SessionService sessionService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.warn("""
                \n
                |===============================================|
                [SECURITY-FILTER] Инициализация LOCAL-режима
                |===============================================|
                """
        );

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .anyRequest().permitAll()
                )
                .userDetailsService(localUserDetailsService())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .addFilterBefore(localAutoAuthentification(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    /**
     * Фильтр для автоматической аутентификации LOCAL-режима.
     */
    private OncePerRequestFilter localAutoAuthentification() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    @NotNull HttpServletRequest request,
                    @NotNull HttpServletResponse response,
                    @NotNull FilterChain chain
            ) throws ServletException, IOException {
                UserDetails localUser = localUserDetailsService()
                        .loadUserByUsername("test@example.com");

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                localUser,
                                null,
                                localUser.getAuthorities()
                        );

                HttpSession session = request.getSession(false);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (Objects.isNull(session)) {
                    sessionService.initialSessionContext(
                            authentication,
                            Map.of(securityProperties.getSession().getRememberMe().getAttributeName(), false)
                    );
                } else {
                    session.setAttribute(
                            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext()
                    );
                }

                chain.doFilter(request, response);
            }
        };
    }

    private UserDetailsService localUserDetailsService() {
        return email -> new SecurityUserDetails(
                userInfoRepository.findByEmail(email)
                        .orElseGet(() -> buildLocalUser(email))
        );
    }

    /**
     * Метод создания тестового пользователя при его отсутствии.
     *
     * @param email E-mail адрес.
     * @return сущность пользователя {@link UserInfoEntity}.
     */
    private UserInfoEntity buildLocalUser(String email) {
        return userInfoRepository.save(
                UserInfoEntity.builder()
                        .email(email)
                        .type(UserType.USER)
                        .passwordHash("")
                        .passwordExpirationDate(LocalDate.now().plusYears(100))
                        .firstName("Тест")
                        .middleName("Тестович")
                        .lastName("Тестов")
                        .build()
        );
    }
}

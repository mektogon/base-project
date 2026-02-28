package ru.dorofeev.security.configuration;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.dorofeev.database.repository.UserInfoRepository;
import ru.dorofeev.security.SecurityProperties;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;
import ru.dorofeev.security.authentication.model.SecurityUserDetails;
import ru.dorofeev.security.cookie.CookieProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class SecurityPromConfiguration {

    private final SecurityProperties securityProperties;

    private final PepperedBCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserInfoRepository userInfoRepository
    ) throws Exception {
        List<String> whiteList = securityProperties.getEndpoint().getWhiteList();
        log.info("""
                        \n
                        |===============================================|
                        [SECURITY-FILTER] Инициализация фильтра с WL: {}
                        |===============================================|
                        """,
                whiteList
        );

        http
                .cors(Customizer.withDefaults())
                .csrf(customizer -> customizer.csrfTokenRepository(cookieCsrfTokenRepository()))
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(whiteList.toArray(String[]::new)).permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService(userInfoRepository))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                buildJsonResponse(response, HttpStatus.UNAUTHORIZED, ErrorType.USER_NOT_AUTHENTICATED)
                        )
                        .accessDeniedHandler((request, response, exception) ->
                                buildJsonResponse(response, HttpStatus.FORBIDDEN, ErrorType.USER_NOT_HAVE_ENOUGH_RIGHTS)
                        )
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        Set<String> allowedMethods = securityProperties.getCors().getAllowedMethods();
        Set<String> allowedOrigins = securityProperties.getCors().getAllowedOrigins();
        Set<String> allowedHeaders = securityProperties.getCors().getAllowedHeaders();
        log.info(
                """
                        \n
                        |===============================================|
                        [SECURITY-CORS] Инициализация CORS-настроек:
                        allowed-origin: {}
                        allowed-methods: {}
                        allowed-headers: {}
                        |===============================================|
                        """,
                allowedOrigins,
                allowedMethods,
                allowedHeaders
        );

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(new ArrayList<>(allowedOrigins));
        configuration.setAllowedMethods(new ArrayList<>(allowedMethods));
        configuration.setAllowedHeaders(new ArrayList<>(allowedHeaders));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(UserInfoRepository userInfoRepository) {
        return email -> new SecurityUserDetails(
                userInfoRepository.findByEmail(email)
                        .orElseThrow(() -> new SecurityException(ErrorType.USER_NOT_FOUND))
        );
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserInfoRepository userInfoRepository
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(
                userDetailsService(userInfoRepository)
        );
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CsrfTokenRepository cookieCsrfTokenRepository() {
        CookieProperties.CsrfCookieProperties cookieProperties = securityProperties.getCookie().getCsrf();
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        repository.setCookieName(cookieProperties.getName());
        repository.setCookieCustomizer(customizer -> {
            customizer.sameSite(cookieProperties.getSameSite());
            customizer.httpOnly(cookieProperties.isHttpOnly());
            customizer.maxAge(cookieProperties.getMaxAge());
            customizer.secure(cookieProperties.isSecure());
            customizer.path(cookieProperties.getPath());
        });
        return repository;
    }

    /**
     * Метод формирования JSON-ответа для {@link HttpServletResponse}.
     *
     * @param response   объект ответа.
     * @param httpStatus возвращаемый HTTP-статус.
     * @param errorType  возвращаемый код ошибки.
     */
    private static void buildJsonResponse(HttpServletResponse response, HttpStatus httpStatus, ErrorType errorType) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.getWriter().write(
                "{\"system\": \"base-project\", \"code\": \"%s\"}"
                        .formatted(errorType.getCode())
        );
    }
}

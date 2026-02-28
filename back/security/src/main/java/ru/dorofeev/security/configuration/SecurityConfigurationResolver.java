package ru.dorofeev.security.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
public class SecurityConfigurationResolver {

    @Configuration
    @EnableWebSecurity
    @Import(ru.dorofeev.security.configuration.SecurityPromConfiguration.class)
    @ConditionalOnProperty(prefix = "security", name = "mode", havingValue = "PROM", matchIfMissing = true)
    public static class SecurityPromConfiguration {

    }

    @Configuration
    @EnableWebSecurity
    @Import(SecurityLocalConfiguration.class)
    @ConditionalOnProperty(prefix = "security", name = "mode", havingValue = "LOCAL")
    public static class SecurityDisabledConfiguration {

    }
}

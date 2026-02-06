package ru.dorofeev.application.ufs.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
public class SettingsProperties {

    @Bean
    @ConfigurationProperties(prefix = "app.regexp")
    public Map<String, String> appRegExp() {
        return new HashMap<>();
    }
}

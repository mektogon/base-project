package ru.dorofeev.application.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"ru.dorofeev.database.entity"})
@EnableJpaRepositories(basePackages = {"ru.dorofeev.database.repository"})
public class DataSourceConfiguration {

}

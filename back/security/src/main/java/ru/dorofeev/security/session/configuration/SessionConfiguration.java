package ru.dorofeev.security.session.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * <a href="https://docs.spring.io/spring-session/reference/configuration/redis.html">см. дополнительно Redis Configuration</a>
 */
@Configuration
public class SessionConfiguration implements BeanClassLoaderAware {

    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(@NotNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Наименование BEAN-компонента быть 'springSessionDefaultRedisSerializer',
     * чтобы переопределить базовый {@link RedisSerializer} используемый в Spring Session.
     *
     * @return объект типа {@link RedisSerializer}.
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * Экземпляр {@link ObjectMapper} для сериализации объектов Redis.
     *
     * @return объект типа {@link ObjectMapper}.
     */
    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.classLoader));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        return mapper;
    }
}

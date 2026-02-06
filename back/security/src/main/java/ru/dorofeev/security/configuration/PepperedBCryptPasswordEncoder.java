package ru.dorofeev.security.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.dorofeev.SecurityProperties;

@Component
public class PepperedBCryptPasswordEncoder implements PasswordEncoder {

    private static final IllegalArgumentException ILLEGAL_PEPPER_EXCEPTION = new IllegalArgumentException(
            "Передан некорректный 'pepper' пароля!"
    );

    private final BCryptPasswordEncoder delegate;
    private final String pepper;

    public PepperedBCryptPasswordEncoder(SecurityProperties securityProperties) {
        this.delegate = new BCryptPasswordEncoder(securityProperties.getPassword().getStrength());
        this.pepper = securityProperties.getPassword().getPepper();

        if (StringUtils.isBlank(pepper)) {
            throw ILLEGAL_PEPPER_EXCEPTION;
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(rawPassword + pepper);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword + pepper, encodedPassword);
    }
}

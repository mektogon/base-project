package ru.dorofeev.security.authentication.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dorofeev.database.entity.UserInfoEntity;
import ru.dorofeev.database.entity.enums.UserStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public record SecurityUserDetails(UserInfoEntity userInfo) implements UserDetails {

    private static final String ROLE_DEFAULT_VALUE = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(
                        ROLE_DEFAULT_VALUE + userInfo.getType().name()
                )
        );
    }

    @Override
    public String getPassword() {
        return userInfo.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !(UserStatus.EXPIRED == userInfo.getUserStatus());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !(UserStatus.BLOCKED == userInfo.getUserStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userInfo.getPasswordExpirationDate().isAfter(LocalDate.now());
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE == userInfo.getUserStatus();
    }
}

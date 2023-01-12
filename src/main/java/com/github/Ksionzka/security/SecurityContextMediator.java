package com.github.Ksionzka.security;

import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityContextMediator {
    private final AppUserService appUserService;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Optional<UserEntity> findCurrentUser() {
        return Optional
            .ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .map(appUserService::loadUserByUsername);
    }

    public static boolean isAuthenticated() {
        return Optional
            .ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .isPresent();
    }

    public UserEntity getCurrentUser() {
        return this.findCurrentUser()
            .orElseThrow(() -> RestException.of(HttpStatus.UNAUTHORIZED, "Unauthorized operation"));
    }
}

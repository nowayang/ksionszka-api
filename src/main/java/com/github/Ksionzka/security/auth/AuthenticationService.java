package com.github.Ksionzka.security.auth;

import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.security.SecurityContextMediator;
import com.github.Ksionzka.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationProvider authenticationProvider;
    private final SecurityContextMediator securityContextMediator;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public AuthenticatedUser authenticate(LoginRequest request) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        );

        try {
            SecurityContextHolder
                .getContext()
                .setAuthentication(this.authenticationProvider.authenticate(credentials));
        } catch (AuthenticationException ex) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Bad credentials");
        }

        return this.getAuthenticatedUser();
    }

    @Transactional(readOnly = true)
    public AuthenticatedUser getAuthenticatedUser() {
        UserEntity currentUser = this.securityContextMediator.getCurrentUser();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUser(currentUser);
        authenticatedUser.setBearerToken(this.jwtService.generateToken(currentUser.getId()));

        return authenticatedUser;
    }
}

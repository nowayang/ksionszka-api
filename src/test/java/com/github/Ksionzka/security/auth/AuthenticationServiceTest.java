package com.github.Ksionzka.security.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.SecurityContextMediator;
import com.github.Ksionzka.security.jwt.JwtService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationService.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private SecurityContextMediator securityContextMediator;

    /**
     * Method under test: {@link AuthenticationService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate() throws JWTCreationException, IllegalArgumentException, AuthenticationException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        when(jwtService.generateToken((Long) any())).thenReturn("ABC123");
        when(authenticationProvider.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        AuthenticatedUser actualAuthenticateResult = authenticationService.authenticate(loginRequest);
        assertEquals("ABC123", actualAuthenticateResult.getBearerToken());
        assertSame(userEntity, actualAuthenticateResult.getUser());
        verify(securityContextMediator).getCurrentUser();
        verify(jwtService).generateToken((Long) any());
        verify(authenticationProvider).authenticate((Authentication) any());
    }

    /**
     * Method under test: {@link AuthenticationService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate3() throws JWTCreationException, IllegalArgumentException, AuthenticationException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        when(jwtService.generateToken((Long) any())).thenReturn("ABC123");
        when(authenticationProvider.authenticate((Authentication) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("iloveyou");
        loginRequest.setUsername("janedoe");
        assertThrows(RestException.class, () -> authenticationService.authenticate(loginRequest));
        verify(authenticationProvider).authenticate((Authentication) any());
    }


    /**
     * Method under test: {@link AuthenticationService#getAuthenticatedUser()}
     */
    @Test
    void testGetAuthenticatedUser() throws JWTCreationException, IllegalArgumentException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        when(jwtService.generateToken((Long) any())).thenReturn("ABC123");
        AuthenticatedUser actualAuthenticatedUser = authenticationService.getAuthenticatedUser();
        assertEquals("ABC123", actualAuthenticatedUser.getBearerToken());
        assertSame(userEntity, actualAuthenticatedUser.getUser());
        verify(securityContextMediator).getCurrentUser();
        verify(jwtService).generateToken((Long) any());
    }

    /**
     * Method under test: {@link AuthenticationService#getAuthenticatedUser()}
     */
    @Test
    void testGetAuthenticatedUser2() throws JWTCreationException, IllegalArgumentException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        when(jwtService.generateToken((Long) any())).thenThrow(new DisabledException("Msg"));
        assertThrows(DisabledException.class, () -> authenticationService.getAuthenticatedUser());
        verify(securityContextMediator).getCurrentUser();
        verify(jwtService).generateToken((Long) any());
    }
}


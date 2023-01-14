package com.github.Ksionzka.security.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.github.Ksionzka.security.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationConfig.class})
@ExtendWith(SpringExtension.class)
class AuthenticationConfigTest {
    @Autowired
    private AuthenticationConfig authenticationConfig;

    @MockBean
    private UserDetailsService userDetailsService;

    /**
     * Method under test: {@link AuthenticationConfig#passwordEncoder()}
     */
    @Test
    void testPasswordEncoder() {
        assertTrue(authenticationConfig.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    /**
     * Method under test: {@link AuthenticationConfig#authenticationProvider(UserDetailsService, PasswordEncoder)}
     */
    @Test
    void testAuthenticationProvider() {
        AppUserService userDetailsService1 = new AppUserService();
        assertTrue(authenticationConfig.authenticationProvider(userDetailsService1,
                new Argon2PasswordEncoder()) instanceof DaoAuthenticationProvider);
    }

    /**
     * Method under test: {@link AuthenticationConfig#authenticationProvider(UserDetailsService, PasswordEncoder)}
     */
    @Test
    void testAuthenticationProvider2() {
        AppUserService userDetailsService1 = mock(AppUserService.class);
        assertTrue(authenticationConfig.authenticationProvider(userDetailsService1,
                new Argon2PasswordEncoder(3, 19088743, 2, 2, 2)) instanceof DaoAuthenticationProvider);
    }
}


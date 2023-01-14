package com.github.Ksionzka.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.registration.token.ConfirmationToken;
import com.github.Ksionzka.security.registration.token.ConfirmationTokenService;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AppUserService.class})
@ExtendWith(SpringExtension.class)
class AppUserServiceTest {
    @Autowired
    private AppUserService appUserService;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AppUserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail((String) any())).thenReturn(ofResult);
        assertSame(userEntity, appUserService.loadUserByUsername("jane.doe@example.org"));
        verify(userRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AppUserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(userRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername("jane.doe@example.org"));
        verify(userRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AppUserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(userRepository.findByEmail((String) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(RestException.class, () -> appUserService.loadUserByUsername("jane.doe@example.org"));
        verify(userRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AppUserService#signUpUser(UserEntity)}
     */
    @Test
    void testSignUpUser2() {
        doNothing().when(confirmationTokenService).saveConfirmationToken((ConfirmationToken) any());
        when(userRepository.save((UserEntity) any())).thenThrow(new UsernameNotFoundException("Msg"));
        when(userRepository.findByEmail((String) any())).thenThrow(new UsernameNotFoundException("Msg"));

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
        assertThrows(UsernameNotFoundException.class, () -> appUserService.signUpUser(userEntity));
        verify(userRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AppUserService#signUpUser(UserEntity)}
     */
    @Test
    void testSignUpUser3() {
        doNothing().when(confirmationTokenService).saveConfirmationToken((ConfirmationToken) any());

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
        when(userRepository.save((UserEntity) any())).thenReturn(userEntity);
        when(userRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setCreatedAt(null);
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setEnabled(true);
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setLastName("Doe");
        userEntity1.setLocked(true);
        userEntity1.setPassword("iloveyou");
        userEntity1.setRole(Role.LIBRARIAN);
        appUserService.signUpUser(userEntity1);
        verify(confirmationTokenService).saveConfirmationToken((ConfirmationToken) any());
        verify(userRepository).save((UserEntity) any());
        verify(userRepository).findByEmail((String) any());
        verify(passwordEncoder).encode((CharSequence) any());
        assertEquals("secret", userEntity1.getPassword());
    }

    /**
     * Method under test: {@link AppUserService#enableAppUser(String)}
     */
    @Test
    void testEnableAppUser() {
        when(userRepository.enableAppUser((String) any())).thenReturn(1);
        assertEquals(1, appUserService.enableAppUser("jane.doe@example.org"));
        verify(userRepository).enableAppUser((String) any());
    }

    /**
     * Method under test: {@link AppUserService#enableAppUser(String)}
     */
    @Test
    void testEnableAppUser2() {
        when(userRepository.enableAppUser((String) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(RestException.class, () -> appUserService.enableAppUser("jane.doe@example.org"));
        verify(userRepository).enableAppUser((String) any());
    }
}


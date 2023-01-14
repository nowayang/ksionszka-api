package com.github.Ksionzka.security.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.security.AppUserService;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.email.EmailSender;
import com.github.Ksionzka.security.registration.token.ConfirmationToken;
import com.github.Ksionzka.security.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RegistrationService.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RegistrationServiceTest {
    @MockBean
    private AppUserService appUserService;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private EmailValidator emailValidator;

    @Autowired
    private RegistrationService registrationService;

    /**
     * Method under test: {@link RegistrationService#register(RegistrationRequest)}
     */
    @Test
    void testRegister() {
        when(appUserService.signUpUser((UserEntity) any())).thenReturn("Sign Up User");
        doNothing().when(emailSender).send((String) any(), (String) any());
        when(emailValidator.test((String) any())).thenReturn(true);
        assertEquals("Sign Up User",
                registrationService.register(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "iloveyou")));
        verify(appUserService).signUpUser((UserEntity) any());
        verify(emailSender).send((String) any(), (String) any());
        verify(emailValidator).test((String) any());
    }

    /**
     * Method under test: {@link RegistrationService#register(RegistrationRequest)}
     */
    @Test
    void testRegister2() {
        when(appUserService.signUpUser((UserEntity) any())).thenReturn("Sign Up User");
        doThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred")).when(emailSender)
                .send((String) any(), (String) any());
        when(emailValidator.test((String) any())).thenReturn(true);
        assertThrows(RestException.class, () -> registrationService
                .register(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "iloveyou")));
        verify(appUserService).signUpUser((UserEntity) any());
        verify(emailSender).send((String) any(), (String) any());
        verify(emailValidator).test((String) any());
    }

}


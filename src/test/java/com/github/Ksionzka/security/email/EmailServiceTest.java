package com.github.Ksionzka.security.email;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.Ksionzka.exception.RestException;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailService.class})
@ExtendWith(SpringExtension.class)
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    /**
     * Method under test: {@link EmailService#send(String, String)}
     */
    @Test
    void testSend() throws MailException {
        doNothing().when(javaMailSender).send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailService.send("alice.liddell@example.org", "jane.doe@example.org", "trallala");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }

    /**
     * Method under test: {@link EmailService#send(String, String)}
     */
    @Test
    void testSend2() throws MailException {
        doThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred")).when(javaMailSender)
                .send((MimeMessage) any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(RestException.class, () -> emailService.send("alice.liddell@example.org", "jane.doe@example.org", "tralalla"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
    }
}


package com.github.Ksionzka.security.email;

import com.github.Ksionzka.exception.RestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Log4j2
public class EmailService implements EmailSender{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("fakemail@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw RestException.of(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send confirmation email");
        }
    }
}

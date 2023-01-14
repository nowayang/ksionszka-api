package com.github.Ksionzka.notification.sender;


import com.github.Ksionzka.notification.ReturnDateNotification;
import com.github.Ksionzka.security.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReturnDateNotificationSender implements ApplicationListener<ReturnDateNotification> {
    private final EmailSender emailSender;

    @Override
    @Async
    public void onApplicationEvent(ReturnDateNotification event) {
        System.out.println("event");
        // todo
    }
}

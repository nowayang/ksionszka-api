package com.github.Ksionzka.security.email;

public interface EmailSender {
    void send(String to, String email, String topic);
}

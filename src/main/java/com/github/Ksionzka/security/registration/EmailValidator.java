package com.github.Ksionzka.security.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String>{
    private final String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean test(String email) {
        System.out.println("-----------Email:" + email);
        return Pattern.compile(REGEX_PATTERN)
                .matcher(email)
                .matches();
    }
}

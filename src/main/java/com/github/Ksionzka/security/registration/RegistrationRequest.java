package com.github.Ksionzka.security.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    @NotBlank(message = "First name must not be blank")
    private final String firstName;

    @NotBlank(message = "Last name must not be blank")
    private final String lastName;

    @NotBlank(message = "Email must not be blank")
    private final String email;

    @NotBlank(message = "Password must not be blank")
    private final String password;
}

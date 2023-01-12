package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;
}

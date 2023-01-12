package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotBlank(message = "New password must not be blank")
    private String newPassword;
}

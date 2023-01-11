package com.github.Ksionzka.controller.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String password;
    private String newPassword;
}

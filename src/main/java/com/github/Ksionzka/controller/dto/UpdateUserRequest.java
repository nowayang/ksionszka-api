package com.github.Ksionzka.controller.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;
    private String name;
}

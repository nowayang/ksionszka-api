package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateLoanRequest {

    @NotNull(message = "Username must not be blank")
    private String username;

    @NotNull(message = "Book ID must not be null")
    private Long bookId;
}

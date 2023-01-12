package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
public class CreateLoanRequest {

    @NotNull(message = "Loan date must not be null")
    private ZonedDateTime loanDate;

    @NotNull(message = "Return date must not be null")
    private ZonedDateTime returnDate;

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Book ID must not be null")
    private String bookId;
}

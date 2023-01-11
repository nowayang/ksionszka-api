package com.github.Ksionzka.controller.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CreateLoanRequest {
    private ZonedDateTime loanDate;
    private ZonedDateTime returnDate;
    private Long userId;
    private String bookId;
}

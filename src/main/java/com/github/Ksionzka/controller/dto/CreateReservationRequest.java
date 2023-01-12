package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateReservationRequest {

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Book ID must not be null")
    private String bookId;
}

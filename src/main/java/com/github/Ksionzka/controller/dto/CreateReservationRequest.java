package com.github.Ksionzka.controller.dto;

import lombok.Data;

@Data
public class CreateReservationRequest {
    private Long userId;
    private String bookId;
}

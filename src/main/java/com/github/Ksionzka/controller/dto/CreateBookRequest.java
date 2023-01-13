package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateBookRequest {

    @NotNull(message = "Release ID must not be null")
    private String releaseId;

    @NotBlank(message = "Number must not be blank")
    private String number;
}

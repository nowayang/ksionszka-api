package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateBookRequest {

    @NotBlank(message = "Number must not be blank")
    private String number;

    @NotNull(message = "Release ID must not be null")
    private String releaseId;
}

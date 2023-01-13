package com.github.Ksionzka.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
public class CreateReleaseRequest {

    @NotNull(message = "ID must not be null")
    private String id;

    @NotBlank(message = "Publisher must not be blank")
    private String publisher;

    @NotNull(message = "Release date must not be null")
    private LocalDate releaseDate;

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotBlank(message = "Language must not be blank")
    private String language;

    @NotBlank(message = "Genre must not be blank")
    private String genre;
}

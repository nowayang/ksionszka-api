package com.github.Ksionzka.controller.dto;

import com.github.Ksionzka.persistence.entity.Genre;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
public class CreateReleaseRequest {

    @NotNull(message = "ID must not be null")
    private String id;

    @NotBlank(message = "Name must not be blank")
    private String title;

    @NotBlank(message = "Publisher must not be blank")
    private String publisher;

    @NotNull(message = "Release date must not be null")
    private LocalDate releaseDate;

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotBlank(message = "Language must not be blank")
    private String language;

    @NotNull(message = "Genre must not be null")
    private Genre genre;
}

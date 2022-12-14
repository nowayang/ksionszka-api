package com.github.Ksionzka.controller.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CreateReleaseRequest {
    private String id;
    private String publisher;
    private ZonedDateTime date;
    private String author;
    private String language;
    private String genre;
}

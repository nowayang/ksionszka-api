package com.github.Ksionzka.controller.dto;

import lombok.Data;

@Data
public class CreateBookRequest {
    private String releaseId;
    private String physicalId;
}

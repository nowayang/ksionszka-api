package com.github.Ksionzka.controller.dto;

import com.github.Ksionzka.persistence.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EnumController {

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return Arrays.asList(Genre.values());
    }
}

package com.github.Ksionzka.controller;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface BaseController<T, K> {
    Page<T> findAll(Pageable pageable, @PathVariable String search);
    T getById(@PathVariable K id);
    void deleteById(@PathVariable K id);

    default String getSearchTerm(String s) {
        return "%" + Optional.ofNullable(s)
            .filter(Strings::isNotBlank)
            .map(String::toLowerCase)
            .orElse(Strings.EMPTY) + "%";
    }
}

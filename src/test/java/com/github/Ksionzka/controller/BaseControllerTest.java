package com.github.Ksionzka.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import org.junit.jupiter.api.Test;

class BaseControllerTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link BaseController#getSearchTerm(String)}
     *   <li>{@link ReleaseController#getSearchTerm(String)}
     * </ul>
     */
    @Test
    void testGetSearchTerm() {
        assertEquals("%foo%", (new ReleaseController(mock(ReleaseRepository.class))).getSearchTerm("foo"));
    }
}


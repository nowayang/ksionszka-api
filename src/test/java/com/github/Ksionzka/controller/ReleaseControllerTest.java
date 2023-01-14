package com.github.Ksionzka.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Ksionzka.controller.dto.CreateReleaseRequest;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;

import java.time.LocalDate;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ReleaseController.class})
@ExtendWith(SpringExtension.class)
class ReleaseControllerTest {
    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ReleaseController releaseController;

    @MockBean
    private ReleaseRepository releaseRepository;


    /**
     * Method under test: {@link ReleaseController#createRelease(CreateReleaseRequest)}
     */
    @Test
    void testCreateRelease2() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setGenre(Genre.Fantastyka);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        releaseEntity.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity.setReleaseYear(1L);
        releaseEntity.setTitle("Dr");
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.existsById((String) any())).thenReturn(false);
        when(releaseRepository.save((ReleaseEntity) any())).thenReturn(releaseEntity);
        ReleaseController releaseController = new ReleaseController(releaseRepository);

        CreateReleaseRequest createReleaseRequest = new CreateReleaseRequest();
        createReleaseRequest.setAuthor("JaneDoe");
        createReleaseRequest.setGenre(Genre.Fantastyka);
        createReleaseRequest.setId("42");
        createReleaseRequest.setLanguage("en");
        createReleaseRequest.setPublisher("Publisher");
        createReleaseRequest.setReleaseDate(LocalDate.ofEpochDay(1L));
        createReleaseRequest.setTitle("Dr");
        assertSame(releaseEntity, releaseController.createRelease(createReleaseRequest));
        verify(releaseRepository).existsById((String) any());
        verify(releaseRepository).save((ReleaseEntity) any());
    }

    /**
     * Method under test: {@link ReleaseController#createRelease(CreateReleaseRequest)}
     */
    @Test
    void testCreateRelease3() {
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.existsById((String) any())).thenThrow(new IllegalArgumentException());
        when(releaseRepository.save((ReleaseEntity) any())).thenThrow(new IllegalArgumentException());
        ReleaseController releaseController = new ReleaseController(releaseRepository);

        CreateReleaseRequest createReleaseRequest = new CreateReleaseRequest();
        createReleaseRequest.setAuthor("JaneDoe");
        createReleaseRequest.setGenre(Genre.Fantastyka);
        createReleaseRequest.setId("42");
        createReleaseRequest.setLanguage("en");
        createReleaseRequest.setPublisher("Publisher");
        createReleaseRequest.setReleaseDate(LocalDate.ofEpochDay(1L));
        createReleaseRequest.setTitle("Dr");
        assertThrows(IllegalArgumentException.class, () -> releaseController.createRelease(createReleaseRequest));
        verify(releaseRepository).existsById((String) any());
    }

    /**
     * Method under test: {@link ReleaseController#deleteById(String)}
     */
    @Test
    void testDeleteById() throws Exception {
        doNothing().when(releaseRepository).deleteById((String) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(String.join("", "/api/", System.getProperty("jdk.debug"), "s/{id}"), "42");
        MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReleaseController#findAll(Pageable)}
     */
    @Test
    void testFindAll() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ReleaseController#getById(String)}
     */
    @Test
    void testGetById() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setGenre(Genre.Fantastyka);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        releaseEntity.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity.setReleaseYear(1L);
        releaseEntity.setTitle("Dr");
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(String.join("", "/api/", System.getProperty("jdk.debug"), "s/{id}"), "42");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(
                contentResult.string(String.join("", "{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\",\"",
                        System.getProperty("jdk.debug"), "Date\":[1970,1,2],\"", System.getProperty("jdk.debug"),
                        "Year\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}")));
    }

    /**
     * Method under test: {@link ReleaseController#updateRelease(String, CreateReleaseRequest)}
     */
    @Test
    void testUpdateRelease() throws Exception {
        CreateReleaseRequest createReleaseRequest = new CreateReleaseRequest();
        createReleaseRequest.setAuthor("JaneDoe");
        createReleaseRequest.setGenre(Genre.Fantastyka);
        createReleaseRequest.setId("42");
        createReleaseRequest.setLanguage("en");
        createReleaseRequest.setPublisher("Publisher");
        createReleaseRequest.setReleaseDate(null);
        createReleaseRequest.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(createReleaseRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(String.join("", "/api/", System.getProperty("jdk.debug"), "s/{id}"), "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}


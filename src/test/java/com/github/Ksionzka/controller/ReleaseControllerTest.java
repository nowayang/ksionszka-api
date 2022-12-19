package com.github.Ksionzka.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Ksionzka.controller.dto.CreateReleaseRequest;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ReleaseController.class})
@ExtendWith(SpringExtension.class)
class ReleaseControllerTest {
    @Autowired
    private ReleaseController releaseController;

    @MockBean
    private ReleaseRepository releaseRepository;

    /**
     * Method under test: {@link ReleaseController#createRelease(CreateReleaseRequest)}
     */
    @Test
    void testCreateRelease() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setDate(null);
        releaseEntity.setGenre("Genre");
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.save((ReleaseEntity) any())).thenReturn(releaseEntity);
        ReleaseController releaseController = new ReleaseController(releaseRepository);

        CreateReleaseRequest createReleaseRequest = new CreateReleaseRequest();
        createReleaseRequest.setAuthor("JaneDoe");
        createReleaseRequest.setDate(null);
        createReleaseRequest.setGenre("Genre");
        createReleaseRequest.setId("42");
        createReleaseRequest.setLanguage("en");
        createReleaseRequest.setPublisher("Publisher");
        assertSame(releaseEntity, releaseController.createRelease(createReleaseRequest));
        verify(releaseRepository).save((ReleaseEntity) any());
    }

    /**
     * Method under test: {@link ReleaseController#createRelease(CreateReleaseRequest)}
     */
    @Test
    void testCreateRelease2() {
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.save((ReleaseEntity) any())).thenThrow(new IllegalArgumentException());
        ReleaseController releaseController = new ReleaseController(releaseRepository);

        CreateReleaseRequest createReleaseRequest = new CreateReleaseRequest();
        createReleaseRequest.setAuthor("JaneDoe");
        createReleaseRequest.setDate(null);
        createReleaseRequest.setGenre("Genre");
        createReleaseRequest.setId("42");
        createReleaseRequest.setLanguage("en");
        createReleaseRequest.setPublisher("Publisher");
        assertThrows(IllegalArgumentException.class, () -> releaseController.createRelease(createReleaseRequest));
        verify(releaseRepository).save((ReleaseEntity) any());
    }

    /**
     * Method under test: {@link ReleaseController#deleteById(String)}
     */
    @Test
    void testDeleteById() {
        doNothing().when(releaseRepository).deleteById((String) any());
        releaseController.deleteById("42");
        verify(releaseRepository).deleteById((String) any());
    }

    /**
     * Method under test: {@link ReleaseController#deleteById(String)}
     */
    @Test
    void testDeleteById2() {
        doThrow(new IllegalArgumentException()).when(releaseRepository).deleteById((String) any());
        assertThrows(IllegalArgumentException.class, () -> releaseController.deleteById("42"));
        verify(releaseRepository).deleteById((String) any());
    }

    /**
     * Method under test: {@link ReleaseController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    /**
     * Method under test: {@link ReleaseController#getById(String)}
     */
    @Test
    void testGetById() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setDate(null);
        releaseEntity.setGenre("Genre");
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/{id}", "42");
        MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"publisher\":\"Publisher\",\"date\":null,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Genre\"}"));
    }

    /**
     * Method under test: {@link ReleaseController#updateRelease(String, CreateReleaseRequest)}
     */
    @Test
    void testUpdateRelease() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setDate(null);
        releaseEntity.setGenre("Genre");
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        ReleaseEntity releaseEntity1 = new ReleaseEntity();
        releaseEntity1.setAuthor("JaneDoe");
        releaseEntity1.setDate(null);
        releaseEntity1.setGenre("Genre");
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");
        when(releaseRepository.save((ReleaseEntity) any())).thenReturn(releaseEntity1);
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity);

        CreateReleaseRequest createReleaseRequest = new CreateReleaseRequest();
        createReleaseRequest.setAuthor("JaneDoe");
        createReleaseRequest.setDate(null);
        createReleaseRequest.setGenre("Genre");
        createReleaseRequest.setId("42");
        createReleaseRequest.setLanguage("en");
        createReleaseRequest.setPublisher("Publisher");
        String content = (new ObjectMapper()).writeValueAsString(createReleaseRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/{id}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(releaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":\"42\",\"publisher\":\"Publisher\",\"date\":null,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Genre\"}"));
    }
}


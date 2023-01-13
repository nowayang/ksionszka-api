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
import com.github.Ksionzka.controller.dto.CreateBookRequest;
import com.github.Ksionzka.controller.dto.UpdateBookRequest;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;

import java.util.Optional;

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

@ContextConfiguration(classes = {BookController.class})
@ExtendWith(SpringExtension.class)
class BookControllerTest {
    @Autowired
    private BookController bookController;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private ReleaseRepository releaseRepository;

    /**
     * Method under test: {@link BookController#createBook(CreateBookRequest)}
     */
    @Test
    @Disabled
    void testCreateBook() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setReleaseDate(null);
        releaseEntity.setGenre(Genre.Biografia);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity);

        ReleaseEntity releaseEntity1 = new ReleaseEntity();
        releaseEntity1.setAuthor("JaneDoe");
        releaseEntity1.setReleaseDate(null);
        releaseEntity1.setGenre(Genre.Biografia);
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity1);
        BookController bookController = new BookController(bookRepository, releaseRepository);

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setPhysicalId("42");
        createBookRequest.setReleaseId("42");
        assertSame(bookEntity, bookController.createBook(createBookRequest));
        verify(bookRepository).save((BookEntity) any());
        verify(releaseRepository).getOrThrowById((String) any());
    }

    /**
     * Method under test: {@link BookController#createBook(CreateBookRequest)}
     */
    @Test
    @Disabled
    void testCreateBook2() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setReleaseDate(null);
        releaseEntity.setGenre(Genre.Biografia);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity);
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.getOrThrowById((String) any())).thenThrow(new IllegalArgumentException());
        BookController bookController = new BookController(bookRepository, releaseRepository);

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setPhysicalId("42");
        createBookRequest.setReleaseId("42");
        assertThrows(IllegalArgumentException.class, () -> bookController.createBook(createBookRequest));
        verify(releaseRepository).getOrThrowById((String) any());
    }

    /**
     * Method under test: {@link BookController#deleteById(String)}
     */
    @Test
    @Disabled
    void testDeleteById() {
        doNothing().when(bookRepository).deleteById((String) any());
        bookController.deleteById("42");
        verify(bookRepository).deleteById((String) any());
    }

    /**
     * Method under test: {@link BookController#deleteById(String)}
     */
    @Test
    @Disabled
    void testDeleteById2() {
        doThrow(new IllegalArgumentException()).when(bookRepository).deleteById((String) any());
        assertThrows(IllegalArgumentException.class, () -> bookController.deleteById("42"));
        verify(bookRepository).deleteById((String) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String)}
     */
    @Test
    @Disabled
    void testFindAll() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    /**
     * Method under test: {@link BookController#getById(String)}
     */
    @Test
    @Disabled
    void testGetById() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setReleaseDate(null);
        releaseEntity.setGenre(Genre.Biografia);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);
        when(bookRepository.getOrThrowById((String) any())).thenReturn(bookEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/{id}", "42");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("", "{\"id\":\"42\",\"", System.getProperty("jdk.debug"),
                "\":{\"id\":\"42\",\"publisher\":\"Publisher\",\"date\":null,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\""
                        + ":\"Genre\"}}")));
    }

    /**
     * Method under test: {@link BookController#updateBook(String, UpdateBookRequest)}
     */
    @Test
    @Disabled
    void testUpdateBook() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setReleaseDate(null);
        releaseEntity.setGenre(Genre.Biografia);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);
        Optional<BookEntity> ofResult = Optional.of(bookEntity);

        ReleaseEntity releaseEntity1 = new ReleaseEntity();
        releaseEntity1.setAuthor("JaneDoe");
        releaseEntity1.setReleaseDate(null);
        releaseEntity1.setGenre(Genre.Biografia);
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId("42");
        bookEntity1.setRelease(releaseEntity1);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity1);
        when(bookRepository.findById((String) any())).thenReturn(ofResult);

        ReleaseEntity releaseEntity2 = new ReleaseEntity();
        releaseEntity2.setAuthor("JaneDoe");
        releaseEntity2.setReleaseDate(null);
        releaseEntity2.setGenre(Genre.Biografia);
        releaseEntity2.setId("42");
        releaseEntity2.setLanguage("en");
        releaseEntity2.setPublisher("Publisher");
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity2);

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setReleaseId("42");
        String content = (new ObjectMapper()).writeValueAsString(updateBookRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/{id}", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("", "{\"id\":\"42\",\"", System.getProperty("jdk.debug"),
                "\":{\"id\":\"42\",\"publisher\":\"Publisher\",\"date\":null,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\""
                        + ":\"Genre\"}}")));
    }
}


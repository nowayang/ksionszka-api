package com.github.Ksionzka.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Ksionzka.controller.dto.CreateBookRequest;
import com.github.Ksionzka.controller.dto.UpdateBookRequest;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll2() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll3() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "", "JaneDoe", "1.0.2", true, true, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll4() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "", "1.0.2", true, true, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll5() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "", true, true, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll6() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", false, true, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll7() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, false, Genre.Fantastyka, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll8() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.SciFi, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll9() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Romans, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll10() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Powieść, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll11() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Fantastyka, "");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll12() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Horror, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll13() {
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(RestException.class, () -> (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Fantastyka, "1.0.2"));
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll14() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Kryminał, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll15() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Thriller, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String, String, String, String, Boolean, Boolean, Genre, String)}
     */
    @Test
    void testFindAll16() {
        BookRepository bookRepository = mock(BookRepository.class);
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = (new BookController(bookRepository, mock(ReleaseRepository.class)))
                .findAll(null, "Search", "Dr", "JaneDoe", "1.0.2", true, true, Genre.Biografia, "1.0.2");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll17() {
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = bookController.findAll(null, "Search");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll18() {
        PageImpl<BookEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any())).thenReturn(pageImpl);
        Page<BookEntity> actualFindAllResult = bookController.findAll(null, "");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll19() {
        when(bookRepository.findAll((Specification<BookEntity>) any(), (Pageable) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(RestException.class, () -> bookController.findAll(null, "Search"));
        verify(bookRepository).findAll((Specification<BookEntity>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookController#createBook(CreateBookRequest)}
     */
    @Test
    void testCreateBook2() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setGenre(Genre.Fantastyka);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        releaseEntity.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity.setReleaseYear(1L);
        releaseEntity.setTitle("Dr");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(123L);
        bookEntity.setNumber("42");
        bookEntity.setRelease(releaseEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.exists((Specification<BookEntity>) any())).thenReturn(false);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity);

        ReleaseEntity releaseEntity1 = new ReleaseEntity();
        releaseEntity1.setAuthor("JaneDoe");
        releaseEntity1.setGenre(Genre.Fantastyka);
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");
        releaseEntity1.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity1.setReleaseYear(1L);
        releaseEntity1.setTitle("Dr");
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity1);
        BookController bookController = new BookController(bookRepository, releaseRepository);

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setNumber("42");
        createBookRequest.setReleaseId("42");
        assertSame(bookEntity, bookController.createBook(createBookRequest));
        verify(bookRepository).exists((Specification<BookEntity>) any());
        verify(bookRepository).save((BookEntity) any());
        verify(releaseRepository).getOrThrowById((String) any());
    }

    /**
     * Method under test: {@link BookController#createBook(CreateBookRequest)}
     */
    @Test
    void testCreateBook3() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setGenre(Genre.Fantastyka);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        releaseEntity.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity.setReleaseYear(1L);
        releaseEntity.setTitle("Dr");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(123L);
        bookEntity.setNumber("42");
        bookEntity.setRelease(releaseEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.exists((Specification<BookEntity>) any())).thenReturn(false);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity);
        ReleaseRepository releaseRepository = mock(ReleaseRepository.class);
        when(releaseRepository.getOrThrowById((String) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        BookController bookController = new BookController(bookRepository, releaseRepository);

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setNumber("42");
        createBookRequest.setReleaseId("42");
        assertThrows(RestException.class, () -> bookController.createBook(createBookRequest));
        verify(bookRepository).exists((Specification<BookEntity>) any());
        verify(releaseRepository).getOrThrowById((String) any());
    }

    /**
     * Method under test: {@link BookController#deleteById(Long)}
     */
    @Test
    void testDeleteById() throws Exception {
        doNothing().when(bookRepository).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/books/{id}", 123L);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link BookController#getById(Long)}
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

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(123L);
        bookEntity.setNumber("42");
        bookEntity.setRelease(releaseEntity);
        when(bookRepository.getOrThrowById((Long) any())).thenReturn(bookEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books/{id}", 123L);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("", "{\"id\":123,\"number\":\"42\",\"",
                System.getProperty("jdk.debug"), "\":{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\",\"",
                System.getProperty("jdk.debug"), "Date\":[1970,1,2],\"", System.getProperty("jdk.debug"),
                "Year\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}}")));
    }

    /**
     * Method under test: {@link BookController#updateBook(Long, UpdateBookRequest)}
     */
    @Test
    void testUpdateBook() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setGenre(Genre.Fantastyka);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        releaseEntity.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity.setReleaseYear(1L);
        releaseEntity.setTitle("Dr");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(123L);
        bookEntity.setNumber("42");
        bookEntity.setRelease(releaseEntity);

        ReleaseEntity releaseEntity1 = new ReleaseEntity();
        releaseEntity1.setAuthor("JaneDoe");
        releaseEntity1.setGenre(Genre.Fantastyka);
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");
        releaseEntity1.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity1.setReleaseYear(1L);
        releaseEntity1.setTitle("Dr");

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId(123L);
        bookEntity1.setNumber("42");
        bookEntity1.setRelease(releaseEntity1);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity1);
        when(bookRepository.getOrThrowById((Long) any())).thenReturn(bookEntity);

        ReleaseEntity releaseEntity2 = new ReleaseEntity();
        releaseEntity2.setAuthor("JaneDoe");
        releaseEntity2.setGenre(Genre.Fantastyka);
        releaseEntity2.setId("42");
        releaseEntity2.setLanguage("en");
        releaseEntity2.setPublisher("Publisher");
        releaseEntity2.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity2.setReleaseYear(1L);
        releaseEntity2.setTitle("Dr");
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity2);

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setNumber("42");
        updateBookRequest.setReleaseId("42");
        String content = (new ObjectMapper()).writeValueAsString(updateBookRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/books/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("", "{\"id\":123,\"number\":\"42\",\"",
                System.getProperty("jdk.debug"), "\":{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\",\"",
                System.getProperty("jdk.debug"), "Date\":[1970,1,2],\"", System.getProperty("jdk.debug"),
                "Year\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}}")));
    }

    /**
     * Method under test: {@link BookController#updateBook(Long, UpdateBookRequest)}
     */
    @Test
    void testUpdateBook2() throws Exception {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setGenre(Genre.Fantastyka);
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");
        releaseEntity.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity.setReleaseYear(1L);
        releaseEntity.setTitle("Dr");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(123L);
        bookEntity.setNumber("?");
        bookEntity.setRelease(releaseEntity);

        ReleaseEntity releaseEntity1 = new ReleaseEntity();
        releaseEntity1.setAuthor("JaneDoe");
        releaseEntity1.setGenre(Genre.Fantastyka);
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");
        releaseEntity1.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity1.setReleaseYear(1L);
        releaseEntity1.setTitle("Dr");

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId(123L);
        bookEntity1.setNumber("42");
        bookEntity1.setRelease(releaseEntity1);
        when(bookRepository.exists((Specification<BookEntity>) any())).thenReturn(false);
        when(bookRepository.save((BookEntity) any())).thenReturn(bookEntity1);
        when(bookRepository.getOrThrowById((Long) any())).thenReturn(bookEntity);

        ReleaseEntity releaseEntity2 = new ReleaseEntity();
        releaseEntity2.setAuthor("JaneDoe");
        releaseEntity2.setGenre(Genre.Fantastyka);
        releaseEntity2.setId("42");
        releaseEntity2.setLanguage("en");
        releaseEntity2.setPublisher("Publisher");
        releaseEntity2.setReleaseDate(LocalDate.ofEpochDay(1L));
        releaseEntity2.setReleaseYear(1L);
        releaseEntity2.setTitle("Dr");
        when(releaseRepository.getOrThrowById((String) any())).thenReturn(releaseEntity2);

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setNumber("42");
        updateBookRequest.setReleaseId("42");
        String content = (new ObjectMapper()).writeValueAsString(updateBookRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/books/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("", "{\"id\":123,\"number\":\"42\",\"",
                System.getProperty("jdk.debug"), "\":{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\",\"",
                System.getProperty("jdk.debug"), "Date\":[1970,1,2],\"", System.getProperty("jdk.debug"),
                "Year\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}}")));
    }
}


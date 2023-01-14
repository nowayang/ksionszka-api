package com.github.Ksionzka.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.Ksionzka.controller.dto.CreateReservationRequest;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.security.AppUserService;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.SecurityContextMediator;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ReservationController.class})
@ExtendWith(SpringExtension.class)
class ReservationControllerTest {
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ReservationController reservationController;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private SecurityContextMediator securityContextMediator;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * Method under test: {@link ReservationController#createReservation(CreateReservationRequest)}
     */
    @Test
    void testCreateReservation2() {
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

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setBook(bookEntity);
        reservationEntity.setCreationDate(null);
        reservationEntity.setId(123L);
        reservationEntity.setReservationDate(null);
        reservationEntity.setUser(userEntity);
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.save((ReservationEntity) any())).thenReturn(reservationEntity);

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
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.getOrThrowById((Long) any())).thenReturn(bookEntity1);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setCreatedAt(null);
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setEnabled(true);
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setLastName("Doe");
        userEntity1.setLocked(true);
        userEntity1.setPassword("iloveyou");
        userEntity1.setRole(Role.LIBRARIAN);
        SecurityContextMediator securityContextMediator = mock(SecurityContextMediator.class);
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity1);
        ReservationController reservationController = new ReservationController(reservationRepository, bookRepository,
                new SimpMessagingTemplate(mock(MessageChannel.class)), securityContextMediator);

        CreateReservationRequest createReservationRequest = new CreateReservationRequest();
        createReservationRequest.setBookId(123L);
        assertSame(reservationEntity, reservationController.createReservation(createReservationRequest));
        verify(reservationRepository).save((ReservationEntity) any());
        verify(bookRepository).getOrThrowById((Long) any());
        verify(securityContextMediator).getCurrentUser();
    }

    /**
     * Method under test: {@link ReservationController#deleteById(String)}
     */
    @Test
    void testDeleteById() throws Exception {
        doNothing().when(reservationRepository).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/reservations/{id}", "42");
        MockMvcBuilders.standaloneSetup(reservationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ReservationController#sendRecentReservations()}
     */
    @Test
    void testSendRecentReservations() throws MessagingException {
        when(reservationRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        doNothing().when(simpMessagingTemplate).convertAndSend((String) any(), (Object) any());
        reservationController.sendRecentReservations();
        verify(reservationRepository).findAll((Pageable) any());
        verify(simpMessagingTemplate).convertAndSend((String) any(), (Object) any());
    }

    /**
     * Method under test: {@link ReservationController#sendRecentReservations()}
     */
    @Test
    void testSendRecentReservations2() throws MessagingException {
        when(reservationRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        doThrow(new IllegalArgumentException()).when(simpMessagingTemplate)
                .convertAndSend((String) any(), (Object) any());
        assertThrows(IllegalArgumentException.class, () -> reservationController.sendRecentReservations());
        verify(reservationRepository).findAll((Pageable) any());
        verify(simpMessagingTemplate).convertAndSend((String) any(), (Object) any());
    }

    /**
     * Method under test: {@link ReservationController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(reservationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ReservationController#getById(String)}
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

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setBook(bookEntity);
        reservationEntity.setCreationDate(null);
        reservationEntity.setId(123L);
        reservationEntity.setReservationDate(null);
        reservationEntity.setUser(userEntity);
        when(reservationRepository.getOrThrowById((Long) any())).thenReturn(reservationEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/reservations/{id}", "42");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(reservationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string("{\"id\":123,\"creationDate\":null,\"reservationDate\":null," +
                "\"user\":{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"role\":\"LIBRARIAN\"," +
                "\"createdAt\":null,\"username\":\"jane.doe@example.org\",\"authorities\":[{\"authority\":\"ROLE_LIBRARIAN\"}]}," +
                "\"book\":{\"id\":123,\"number\":\"42\",\"release\":{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\"," +
                "\"releaseDate\":[1970,1,2],\"releaseYear\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}}}"));
    }
}


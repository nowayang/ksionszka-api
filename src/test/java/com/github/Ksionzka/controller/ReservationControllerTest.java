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
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepositoryOLD;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
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
    private UserRepositoryOLD userRepository;

    /**
     * Method under test: {@link ReservationController#createReservation(CreateReservationRequest)}
     */
    @Test
    void testCreateReservation() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setDate(null);
        releaseEntity.setGenre("Genre");
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(123L);
        userEntity.setPassword("iloveyou");
        userEntity.setRole("Role");

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
        releaseEntity1.setDate(null);
        releaseEntity1.setGenre("Genre");
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId("42");
        bookEntity1.setRelease(releaseEntity1);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.getOrThrowById((String) any())).thenReturn(bookEntity1);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setId(123L);
        userEntity1.setPassword("iloveyou");
        userEntity1.setRole("Role");
        UserRepositoryOLD userRepository = mock(UserRepositoryOLD.class);
        when(userRepository.getOrThrowById((Long) any())).thenReturn(userEntity1);
        ReservationController reservationController = new ReservationController(reservationRepository, bookRepository,
                userRepository);

        CreateReservationRequest createReservationRequest = new CreateReservationRequest();
        createReservationRequest.setBookId("42");
        createReservationRequest.setUserId(123L);
        assertSame(reservationEntity, reservationController.createReservation(createReservationRequest));
        verify(reservationRepository).save((ReservationEntity) any());
        verify(bookRepository).getOrThrowById((String) any());
        verify(userRepository).getOrThrowById((Long) any());
    }

    /**
     * Method under test: {@link ReservationController#createReservation(CreateReservationRequest)}
     */
    @Test
    void testCreateReservation2() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setAuthor("JaneDoe");
        releaseEntity.setDate(null);
        releaseEntity.setGenre("Genre");
        releaseEntity.setId("42");
        releaseEntity.setLanguage("en");
        releaseEntity.setPublisher("Publisher");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(123L);
        userEntity.setPassword("iloveyou");
        userEntity.setRole("Role");

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
        releaseEntity1.setDate(null);
        releaseEntity1.setGenre("Genre");
        releaseEntity1.setId("42");
        releaseEntity1.setLanguage("en");
        releaseEntity1.setPublisher("Publisher");

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId("42");
        bookEntity1.setRelease(releaseEntity1);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.getOrThrowById((String) any())).thenReturn(bookEntity1);
        UserRepositoryOLD userRepository = mock(UserRepositoryOLD.class);
        when(userRepository.getOrThrowById((Long) any())).thenThrow(new IllegalArgumentException());
        ReservationController reservationController = new ReservationController(reservationRepository, bookRepository,
                userRepository);

        CreateReservationRequest createReservationRequest = new CreateReservationRequest();
        createReservationRequest.setBookId("42");
        createReservationRequest.setUserId(123L);
        assertThrows(IllegalArgumentException.class,
                () -> reservationController.createReservation(createReservationRequest));
        verify(bookRepository).getOrThrowById((String) any());
        verify(userRepository).getOrThrowById((Long) any());
    }

    /**
     * Method under test: {@link ReservationController#deleteById(String)}
     */
    @Test
    void testDeleteById() {
        doNothing().when(reservationRepository).deleteById((String) any());
        reservationController.deleteById("42");
        verify(reservationRepository).deleteById((String) any());
    }

    /**
     * Method under test: {@link ReservationController#deleteById(String)}
     */
    @Test
    void testDeleteById2() {
        doThrow(new IllegalArgumentException()).when(reservationRepository).deleteById((String) any());
        assertThrows(IllegalArgumentException.class, () -> reservationController.deleteById("42"));
        verify(reservationRepository).deleteById((String) any());
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
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    /**
     * Method under test: {@link ReservationController#getById(String)}
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

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("42");
        bookEntity.setRelease(releaseEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(123L);
        userEntity.setPassword("iloveyou");
        userEntity.setRole("Role");

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setBook(bookEntity);
        reservationEntity.setCreationDate(null);
        reservationEntity.setId(123L);
        reservationEntity.setReservationDate(null);
        reservationEntity.setUser(userEntity);
        when(reservationRepository.getOrThrowById((String) any())).thenReturn(reservationEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/{id}", "42");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(reservationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("",
                "{\"id\":123,\"creationDate\":null,\"reservationDate\":null,\"user\":{\"id\":123,\"email\":\"jane.doe@example.org\""
                        + ",\"password\":\"iloveyou\",\"role\":\"Role\"},\"book\":{\"id\":\"42\",\"",
                System.getProperty("jdk.debug"),
                "\":{\"id\":\"42\",\"publisher\":\"Publisher\",\"date\":null,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":"
                        + "\"Genre\"}}}")));
    }
}


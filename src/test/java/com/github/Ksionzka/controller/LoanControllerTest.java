package com.github.Ksionzka.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.Ksionzka.controller.dto.CreateLoanRequest;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.LoanRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.AppUserService;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.SecurityContextMediator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LoanController.class})
@ExtendWith(SpringExtension.class)
class LoanControllerTest {
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private LoanController loanController;

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private SecurityContextMediator securityContextMediator;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link LoanController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll2() {
        PageImpl<LoanEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(loanRepository.findAll((Specification<LoanEntity>) any(), (Pageable) any())).thenReturn(pageImpl);

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
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        Page<LoanEntity> actualFindAllResult = loanController.findAll(null, "Search");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(loanRepository).findAll((Specification<LoanEntity>) any(), (Pageable) any());
        verify(securityContextMediator).getCurrentUser();
    }

    /**
     * Method under test: {@link LoanController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll3() {
        when(loanRepository.findAll((Specification<LoanEntity>) any(), (Pageable) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));

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
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        assertThrows(RestException.class, () -> loanController.findAll(null, "Search"));
        verify(loanRepository).findAll((Specification<LoanEntity>) any(), (Pageable) any());
        verify(securityContextMediator).getCurrentUser();
    }

    /**
     * Method under test: {@link LoanController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll4() {
        PageImpl<LoanEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(loanRepository.findAll((Specification<LoanEntity>) any(), (Pageable) any())).thenReturn(pageImpl);

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.USER);
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        Page<LoanEntity> actualFindAllResult = loanController.findAll(null, "Search");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(loanRepository).findAll((Specification<LoanEntity>) any(), (Pageable) any());
        verify(securityContextMediator).getCurrentUser();
    }

    /**
     * Method under test: {@link LoanController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll5() {
        PageImpl<LoanEntity> pageImpl = new PageImpl<>(new ArrayList<>());
        when(loanRepository.findAll((Specification<LoanEntity>) any(), (Pageable) any())).thenReturn(pageImpl);

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
        when(securityContextMediator.getCurrentUser()).thenReturn(userEntity);
        Page<LoanEntity> actualFindAllResult = loanController.findAll(null, "");
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(loanRepository).findAll((Specification<LoanEntity>) any(), (Pageable) any());
        verify(securityContextMediator).getCurrentUser();
    }



    /**
     * Method under test: {@link LoanController#createLoan(CreateLoanRequest)}
     */
    @Test
    void testCreateLoan2() {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);
        LoanRepository loanRepository = mock(LoanRepository.class);
        when(loanRepository.save((LoanEntity) any())).thenReturn(loanEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.exists((Long) any(), (Specification<BookEntity>) any()))
                .thenThrow(new IllegalArgumentException());
        when(bookRepository.getOrThrowById((Long) any())).thenThrow(new IllegalArgumentException());

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
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail((String) any())).thenReturn(Optional.of(userEntity1));
        LoanController loanController = new LoanController(loanRepository, bookRepository, userRepository,
                new SecurityContextMediator(new AppUserService()), mock(ReservationRepository.class));

        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setBookId(123L);
        createLoanRequest.setUsername("janedoe");
        assertThrows(IllegalArgumentException.class, () -> loanController.createLoan(createLoanRequest));
        verify(bookRepository).exists((Long) any(), (Specification<BookEntity>) any());
        verify(userRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link LoanController#createLoan(CreateLoanRequest)}
     */
    @Test
    void testCreateLoan3() {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);
        LoanRepository loanRepository = mock(LoanRepository.class);
        when(loanRepository.save((LoanEntity) any())).thenReturn(loanEntity);

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
        when(bookRepository.exists((Long) any(), (Specification<BookEntity>) any())).thenReturn(false);
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
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail((String) any())).thenReturn(Optional.of(userEntity1));
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findAll((Specification<ReservationEntity>) any())).thenReturn(new ArrayList<>());
        doNothing().when(reservationRepository).deleteAll((Iterable<ReservationEntity>) any());
        LoanController loanController = new LoanController(loanRepository, bookRepository, userRepository,
                new SecurityContextMediator(new AppUserService()), reservationRepository);

        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setBookId(123L);
        createLoanRequest.setUsername("janedoe");
        assertSame(loanEntity, loanController.createLoan(createLoanRequest));
        verify(loanRepository).save((LoanEntity) any());
        verify(bookRepository).exists((Long) any(), (Specification<BookEntity>) any());
        verify(bookRepository).getOrThrowById((Long) any());
        verify(userRepository).findByEmail((String) any());
        verify(reservationRepository).findAll((Specification<ReservationEntity>) any());
        verify(reservationRepository).deleteAll((Iterable<ReservationEntity>) any());
    }

    /**
     * Method under test: {@link LoanController#returnLoan(Long)}
     */
    @Test
    void testReturnLoan2() {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);

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

        LoanEntity loanEntity1 = new LoanEntity();
        loanEntity1.setActualReturnDate(null);
        loanEntity1.setBook(bookEntity1);
        loanEntity1.setId(123L);
        loanEntity1.setLoanDate(null);
        loanEntity1.setRequestedReturnDateExtensionAt(null);
        loanEntity1.setReturnDate(null);
        loanEntity1.setUser(userEntity1);
        LoanRepository loanRepository = mock(LoanRepository.class);
        when(loanRepository.save((LoanEntity) any())).thenReturn(loanEntity1);
        when(loanRepository.getOrThrowById((Long) any())).thenReturn(loanEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.exists((Long) any(), (Specification<BookEntity>) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        UserRepository userRepository = mock(UserRepository.class);
        assertThrows(RestException.class, () -> (new LoanController(loanRepository, bookRepository, userRepository,
                new SecurityContextMediator(new AppUserService()), mock(ReservationRepository.class))).returnLoan(123L));
        verify(loanRepository).getOrThrowById((Long) any());
        verify(bookRepository).exists((Long) any(), (Specification<BookEntity>) any());
    }

    /**
     * Method under test: {@link LoanController#returnLoan(Long)}
     */
    @Test
    void testReturnLoan3() {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);

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

        LoanEntity loanEntity1 = new LoanEntity();
        loanEntity1.setActualReturnDate(null);
        loanEntity1.setBook(bookEntity1);
        loanEntity1.setId(123L);
        loanEntity1.setLoanDate(null);
        loanEntity1.setRequestedReturnDateExtensionAt(null);
        loanEntity1.setReturnDate(null);
        loanEntity1.setUser(userEntity1);
        LoanRepository loanRepository = mock(LoanRepository.class);
        when(loanRepository.save((LoanEntity) any())).thenReturn(loanEntity1);
        when(loanRepository.getOrThrowById((Long) any())).thenReturn(loanEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.exists((Long) any(), (Specification<BookEntity>) any())).thenReturn(false);
        UserRepository userRepository = mock(UserRepository.class);
        assertSame(loanEntity1, (new LoanController(loanRepository, bookRepository, userRepository,
                new SecurityContextMediator(new AppUserService()), mock(ReservationRepository.class))).returnLoan(123L));
        verify(loanRepository).save((LoanEntity) any());
        verify(loanRepository).getOrThrowById((Long) any());
        verify(bookRepository).exists((Long) any(), (Specification<BookEntity>) any());
    }

    /**
     * Method under test: {@link LoanController#returnLoan(Long)}
     */
    @Test
    void testReturnLoan4() {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);
        LoanRepository loanRepository = mock(LoanRepository.class);
        when(loanRepository.save((LoanEntity) any()))
                .thenThrow(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        when(loanRepository.getOrThrowById((Long) any())).thenReturn(loanEntity);
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.exists((Long) any(), (Specification<BookEntity>) any())).thenReturn(false);
        UserRepository userRepository = mock(UserRepository.class);
        assertThrows(RestException.class, () -> (new LoanController(loanRepository, bookRepository, userRepository,
                new SecurityContextMediator(new AppUserService()), mock(ReservationRepository.class))).returnLoan(1L));
        verify(loanRepository).save((LoanEntity) any());
        verify(loanRepository).getOrThrowById((Long) any());
        verify(bookRepository).exists((Long) any(), (Specification<BookEntity>) any());
    }

    /**
     * Method under test: {@link LoanController#deleteById(Long)}
     */
    @Test
    void testDeleteById() throws Exception {
        doNothing().when(loanRepository).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/loans/{id}", 123L);
        MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link LoanController#extendLoan(Long)}
     */
    @Test
    void testExtendLoan() throws Exception {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);
        when(loanRepository.exists((Long) any(), (Specification<LoanEntity>) any())).thenReturn(true);
        when(loanRepository.getOrThrowById((Long) any())).thenReturn(loanEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/loans/{id}/extend",
                "Uri Variables", "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link LoanController#findAll(Pageable, String, Boolean, Boolean, Boolean)}
     */
    @Test
    void testFindAll() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link LoanController#getById(Long)}
     */
    @Test
    @Disabled
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);
        when(loanRepository.getOrThrowById((Long) any())).thenReturn(loanEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/loans/{id}", 123L);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string("{\"id\":123,\"loanDate\":null,\"returnDate\":null,\"actualReturnDate\":null,\"user\":{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"role\":\"LIBRARIAN\",\"createdAt\":null,\"username\":\"jane.doe@example.org\",\"authorities\":[{\"authority\":\"ROLE_LIBRARIAN\"}]},\"book\":{\"id\":123,\"number\":\"42\",\"release\":{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\",\"releaseDate\":[1970,1,2],\"releaseYear\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}},\"requestedReturnDateExtensionAt\":null}"));
    }

    /**
     * Method under test: {@link LoanController#requestReturnDateExtension(Long)}
     */
    @Test
    @Disabled
    void testRequestReturnDateExtension() throws Exception {
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

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setActualReturnDate(null);
        loanEntity.setBook(bookEntity);
        loanEntity.setId(123L);
        loanEntity.setLoanDate(null);
        loanEntity.setRequestedReturnDateExtensionAt(null);
        loanEntity.setReturnDate(null);
        loanEntity.setUser(userEntity);

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

        LoanEntity loanEntity1 = new LoanEntity();
        loanEntity1.setActualReturnDate(null);
        loanEntity1.setBook(bookEntity1);
        loanEntity1.setId(123L);
        loanEntity1.setLoanDate(null);
        loanEntity1.setRequestedReturnDateExtensionAt(null);
        loanEntity1.setReturnDate(null);
        loanEntity1.setUser(userEntity1);
        when(loanRepository.save((LoanEntity) any())).thenReturn(loanEntity1);
        when(loanRepository.exists((Long) any(), (Specification<LoanEntity>) any())).thenReturn(false);
        when(loanRepository.getOrThrowById((Long) any())).thenReturn(loanEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/loans/{id}/request-extension",
                123L);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string("{\"id\":123,\"loanDate\":null,\"returnDate\":null,\"actualReturnDate\":null," +
                "\"user\":{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"role\":\"LIBRARIAN\"," +
                "\"createdAt\":null,\"username\":\"jane.doe@example.org\",\"authorities\":[{\"authority\":\"ROLE_LIBRARIAN\"}]}," +
                "\"book\":{\"id\":123,\"number\":\"42\",\"release\":{\"id\":\"42\",\"title\":\"Dr\",\"publisher\":\"Publisher\"," +
                "\"releaseDate\":[1970,1,2],\"releaseYear\":1,\"author\":\"JaneDoe\",\"language\":\"en\",\"genre\":\"Fantastyka\"}},\"requestedReturnDateExtensionAt\":null}"));
    }
}


package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateLoanRequest;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.LoanRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.persistence.specification.BookSpecifications;
import com.github.Ksionzka.persistence.specification.LoanSpecifications;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.SecurityContextMediator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController implements BaseController<LoanEntity, Long> {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final SecurityContextMediator securityContextMediator;
    private final ReservationRepository reservationRepository;

    @GetMapping()
    @Transactional(readOnly = true)
    public Page<LoanEntity> findAll(Pageable pageable, @RequestParam(required = false) String search) {
        final String searchTerm = this.getSearchTerm(search);

        Specification<LoanEntity> specification = Specification.where(null);

        if (Strings.isNotBlank(search)) {
            specification = specification.and((root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("user").get("email")), searchTerm),
                cb.like(cb.lower(root.get("book").get("number")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("title")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("publisher")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("author")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("genre").as(String.class)), searchTerm)
            ));
        }

        UserEntity currentUser = this.securityContextMediator.getCurrentUser();
        if (Role.USER.equals(currentUser.getRole())) {
            specification = specification.and((root, cq, cb) -> cb.equal(root.get("user").get("id"), currentUser.getId()));
        }

        return this.loanRepository.findAll(specification, pageable);
    }

    @GetMapping("/users/{userId}")
    @Transactional(readOnly = true)
    public Page<LoanEntity> findUserLoans(Pageable pageable, @PathVariable Long userId) {
        return this.loanRepository.findAll(
            (root, cq, cb) -> cb.equal(root.get("user").get("id"), userId),
            pageable
        );
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public LoanEntity getById(@PathVariable Long id) {
        return this.loanRepository.getOrThrowById(id);
    }

    @PostMapping
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public LoanEntity createLoan(@Valid @RequestBody CreateLoanRequest createLoanRequest) {
        LoanEntity loanEntity = new LoanEntity();

        loanEntity.setUser(
            this.userRepository
                .findByEmail(createLoanRequest.getUsername())
                .orElseThrow(() -> RestException.of(HttpStatus.BAD_REQUEST, "User with email not exists"))
        );

        if (this.bookRepository.exists(createLoanRequest.getBookId(), BookSpecifications.isLoaned())) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Book is loaned or reserved");
        }

        loanEntity.setBook(this.bookRepository.getOrThrowById(createLoanRequest.getBookId()));
        loanEntity.setLoanDate(ZonedDateTime.now());
        loanEntity.setReturnDate(ZonedDateTime.now().plusWeeks(1));

        this.reservationRepository.deleteAll(
            this.reservationRepository.findAll((root, cq, cb) -> cb.equal(
                root.get("book").get("id"), createLoanRequest.getBookId()))
        );

        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/return")
    @Transactional
    public LoanEntity returnLoan(@PathVariable Long id) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);

        if (!this.bookRepository.exists(id, BookSpecifications.isLoaned())) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Book is already returned");
        }

        loanEntity.setActualReturnDate(ZonedDateTime.now());
        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/extend")
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public LoanEntity extendLoan(@PathVariable Long id) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);

        if (this.loanRepository.exists(id, LoanSpecifications.isDelayed())) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Cannot extend delayed loan");
        }

        if (this.loanRepository.exists(id, (root, cq, cb) -> cb.isNotNull(root.get("actualReturnDate")))) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Cannot extend returned loan");
        }

        Optional.ofNullable(loanEntity.getRequestedReturnDateExtensionAt()).ifPresentOrElse(
            date -> {
                loanEntity.setRequestedReturnDateExtensionAt(null);
                loanEntity.setReturnDate(date.plusWeeks(1));
            },
            () -> loanEntity.setReturnDate(ZonedDateTime.now().plusWeeks(1))
        );

        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/request-extension")
    @Transactional
    public LoanEntity requestReturnDateExtension(@PathVariable Long id) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);

        if (this.loanRepository.exists(id, (root, cq, cb) -> cb.isNotNull(root.get("requestedReturnDateExtensionAt")))) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Loan is already requested to extend");
        }

        if (this.loanRepository.exists(id, LoanSpecifications.isDelayed())) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Cannot extend delayed loan");
        }

        if (this.loanRepository.exists(id, (root, cq, cb) -> cb.isNotNull(root.get("actualReturnDate")))) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Cannot extend returned loan");
        }

        loanEntity.setRequestedReturnDateExtensionAt(ZonedDateTime.now());
        return this.loanRepository.save(loanEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public void deleteById(@PathVariable Long id) {
        this.loanRepository.deleteById(Long.valueOf(id));
    }
}

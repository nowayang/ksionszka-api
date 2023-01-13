package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateLoanRequest;
import com.github.Ksionzka.controller.dto.CreateReservationRequest;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.LoanRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.SecurityContextMediator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
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

    @GetMapping()
    @Transactional(readOnly = true)
    public Page<LoanEntity> findAll(Pageable pageable, @RequestParam(required = false) String search) {
        final String searchTerm = this.getSearchTerm(search);

        Specification<LoanEntity> specification = Specification.where(null);

        if (Strings.isNotBlank(search)) {
            specification = specification.and((root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("user").get("email")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("publisher")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("author")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("genre")), searchTerm)
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
    public LoanEntity createLoan(@Valid @RequestBody CreateLoanRequest createLoanRequest) {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setUser(this.userRepository.getOrThrowById(createLoanRequest.getUserId()));
        loanEntity.setBook(this.bookRepository.getOrThrowById(createLoanRequest.getBookId()));
        loanEntity.setLoanDate(createLoanRequest.getLoanDate());
        loanEntity.setReturnDate(createLoanRequest.getReturnDate());

        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/return")
    @Transactional
    public LoanEntity returnLoan(@PathVariable Long id) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);

        //todo nie mozna oddac oddanej ksiazki
        loanEntity.setActualReturnDate(ZonedDateTime.now());
        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/extend")
    @Transactional
    public LoanEntity extendLoan(@PathVariable Long id) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);

        //todo nie mozna przedluzyc spoznionej ksiązki
        ZonedDateTime dateTime = Optional.ofNullable(loanEntity.getRequestedReturnDateExtensionAt()).orElse(ZonedDateTime.now());
        loanEntity.setReturnDate(dateTime.plusWeeks(1));
        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/request-extension")
    @Transactional
    public LoanEntity requestReturnDateExtension(@PathVariable Long id) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);

        //todo nie można zrequestować oddanej książki i spoznionej ksiązki
        loanEntity.setRequestedReturnDateExtensionAt(ZonedDateTime.now());
        return this.loanRepository.save(loanEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable Long id) {
        this.loanRepository.deleteById(Long.valueOf(id));
    }
}

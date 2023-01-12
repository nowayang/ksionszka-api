package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateLoanRequest;
import com.github.Ksionzka.controller.dto.CreateReservationRequest;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.LoanRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController implements BaseController<LoanEntity, Long> {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @GetMapping()
    @Transactional(readOnly = true)
    public Page<LoanEntity> findAll(Pageable pageable, @RequestParam String search) {
        final String searchTerm = this.getSearchTerm(search);
        return this.loanRepository.findAll(
            (Specification<LoanEntity>)
                (root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("user").get("email")), searchTerm),
                    cb.like(cb.lower(root.get("book").get("release").get("publisher")), searchTerm),
                    cb.like(cb.lower(root.get("book").get("release").get("author")), searchTerm),
                    cb.like(cb.lower(root.get("book").get("release").get("genre")), searchTerm)
                ),
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
        loanEntity.setActualReturnDate(ZonedDateTime.now());
        return this.loanRepository.save(loanEntity);
    }

    @PostMapping("/{id}/extend")
    @Transactional
    public LoanEntity extendLoan(@PathVariable Long id,
                                 @Valid @NotNull(message = "Return date must not be null") @RequestBody ZonedDateTime returnDate) {
        LoanEntity loanEntity = this.loanRepository.getOrThrowById(id);
        loanEntity.setReturnDate(returnDate);
        return this.loanRepository.save(loanEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable Long id) {
        this.loanRepository.deleteById(Long.valueOf(id));
    }
}

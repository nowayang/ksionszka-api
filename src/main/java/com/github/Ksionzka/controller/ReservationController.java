package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateReservationRequest;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController implements BaseController<ReservationEntity, String> {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @GetMapping
    @Transactional(readOnly = true)
    public Page<ReservationEntity> findAll(Pageable pageable, @PathVariable String search) {
        final String searchTerm = this.getSearchTerm(search);
        return this.reservationRepository.findAll(
            (Specification<ReservationEntity>)
                (root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("user").get("email")), searchTerm),
                    cb.like(cb.lower(root.get("book").get("publisher")), searchTerm),
                    cb.like(cb.lower(root.get("book").get("author")), searchTerm),
                    cb.like(cb.lower(root.get("book").get("genre")), searchTerm)
                ),
            pageable
        );
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ReservationEntity getById(@PathVariable String id) {
        return this.reservationRepository.getOrThrowById(id);
    }

    @PostMapping
    @Transactional
    public ReservationEntity createReservation(@RequestBody CreateReservationRequest request) {
        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setBook(this.bookRepository.getOrThrowById(request.getBookId()));
        reservationEntity.setReservationDate(ZonedDateTime.now().plusDays(2));
        reservationEntity.setUser(this.userRepository.getOrThrowById(request.getUserId()));
        reservationEntity.setCreationDate(ZonedDateTime.now());

        return this.reservationRepository.save(reservationEntity);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.reservationRepository.deleteById(id);
    }
}

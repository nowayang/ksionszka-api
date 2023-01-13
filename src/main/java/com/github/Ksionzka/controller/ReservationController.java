package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateReservationRequest;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReservationRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.Role;
import com.github.Ksionzka.security.SecurityContextMediator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController implements BaseController<ReservationEntity, String> {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SecurityContextMediator securityContextMediator;

    @Override
    public Page<ReservationEntity> findAll(Pageable pageable, String search) {
        return this.findAll(pageable, search, null, null, null);
    }

    @GetMapping()
    @Transactional(readOnly = true)
    public Page<ReservationEntity> findAll(Pageable pageable,
                                           @RequestParam(required = false) String search,
                                           @RequestParam(required = false) String bookNameLike,
                                           @RequestParam(required = false) String authorLike,
                                           @RequestParam(required = false) String releaseIdLike) {
        final String searchTerm = this.getSearchTerm(search);

        Specification<ReservationEntity> specification = Specification.where(null);

        if (Strings.isNotBlank(search)) {
            specification = specification.and((root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("user").get("email")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("publisher")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("author")), searchTerm),
                cb.like(cb.lower(root.get("book").get("release").get("genre")), searchTerm)
            ));
        }

        if (Strings.isNotBlank(bookNameLike)) {
            specification = specification.and((root, cq, cb) -> cb.like(
                cb.lower(root.get("book").get("name")), this.getSearchTerm(bookNameLike)));
        }

        if (Strings.isNotBlank(authorLike)) {
            specification = specification.and((root, cq, cb) -> cb.like(
                cb.lower(root.get("book").get("release").get("author")), this.getSearchTerm(authorLike)));
        }

        if (Strings.isNotBlank(releaseIdLike)) {
            specification = specification.and((root, cq, cb) -> cb.like(
                cb.lower(root.get("book").get("release").get("id")), this.getSearchTerm(releaseIdLike)));
        }

        UserEntity currentUser = this.securityContextMediator.getCurrentUser();
        if (Role.USER.equals(currentUser.getRole())) {
            specification = specification.and((root, cq, cb) -> cb.equal(root.get("user").get("id"), currentUser.getId()));
        }

        return this.reservationRepository.findAll(specification, pageable);
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ReservationEntity getById(@PathVariable String id) {
        return this.reservationRepository.getOrThrowById(Long.valueOf(id));
    }

    @PostMapping
    @Transactional
    public ReservationEntity createReservation(@Valid @RequestBody CreateReservationRequest request) {
        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setBook(this.bookRepository.getOrThrowById(request.getBookId()));
        reservationEntity.setReservationDate(ZonedDateTime.now().plusDays(2));
        reservationEntity.setUser(this.userRepository.getOrThrowById(request.getUserId()));
        reservationEntity.setCreationDate(ZonedDateTime.now());

        return this.reservationRepository.save(reservationEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable String id) {
        this.reservationRepository.deleteById(Long.valueOf(id));
    }

    @Scheduled(fixedRate = 15000)
    public void sendRecentReservations() {
        simpMessagingTemplate.convertAndSend("/topic/recent-reservations",
                reservationRepository.findAll(
                        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationDate"))
                ).getContent()
        );
    }
}

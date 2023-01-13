package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateBookRequest;
import com.github.Ksionzka.controller.dto.UpdateBookRequest;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import com.github.Ksionzka.persistence.specification.BookSpecifications;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController implements BaseController<BookEntity, String> {
    private final BookRepository bookRepository;
    private final ReleaseRepository releaseRepository;

    @Override
    public Page<BookEntity> findAll(Pageable pageable, String search) {
        return this.findAll(pageable, search, null, null, null, null,
            null, null, null);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<BookEntity> findAll(Pageable pageable,
                                    @RequestParam(required = false) String search,
                                    @RequestParam(required = false) String bookNameLike,
                                    @RequestParam(required = false) String authorLike,
                                    @RequestParam(required = false) String releaseIdLike,
                                    @RequestParam(required = false) Boolean loaned,
                                    @RequestParam(required = false) Boolean reserved,
                                    @RequestParam(required = false) Genre genre,
                                    @RequestParam(required = false) Long releaseYear) {
        final String searchTerm = this.getSearchTerm(search);
        Specification<BookEntity> specification = Specification.where(null);

        if (Strings.isNotBlank(search)) {
            specification = specification.and((root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("id")), searchTerm),
                cb.like(cb.lower(root.get("release").get("id")), searchTerm),
                cb.like(cb.lower(root.get("release").get("publisher")), searchTerm),
                cb.like(cb.lower(root.get("release").get("author")), searchTerm),
                cb.like(cb.lower(root.get("release").get("genre").as(String.class)), searchTerm)
            ));
        }

        if (Strings.isNotBlank(bookNameLike)) {
            specification = specification.and((root, cq, cb) -> cb.like(
                cb.lower(root.get("name")), this.getSearchTerm(bookNameLike)));
        }

        if (Strings.isNotBlank(authorLike)) {
            specification = specification.and((root, cq, cb) -> cb.like(
                cb.lower(root.get("release").get("author")), this.getSearchTerm(authorLike)));
        }

        if (Strings.isNotBlank(releaseIdLike)) {
            specification = specification.and((root, cq, cb) -> cb.like(
                cb.lower(root.get("release").get("id")), this.getSearchTerm(releaseIdLike)));
        }

        if (Objects.nonNull(loaned)) {
            Specification<BookEntity> isLoanedSpecification = BookSpecifications.isLoaned();
            specification = specification.and(loaned ? isLoanedSpecification : Specification.not(isLoanedSpecification));
        }

        if (Objects.nonNull(reserved)) {
            Specification<BookEntity> isReservedSpecification = BookSpecifications.isReserved();
            specification = specification.and(reserved ? isReservedSpecification : Specification.not(isReservedSpecification));
        }

        if (Objects.nonNull(genre)) {
            specification = specification.and((root, cq, cb) -> cb.equal(root.get("release").get("genre"), genre));
        }

        if (Objects.nonNull(releaseYear)) {
            specification = specification.and((root, cq, cb) -> cb.equal(root.get("release").get("releaseYear"), genre));
        }

        return this.bookRepository.findAll(specification, pageable);
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public BookEntity getById(@PathVariable String id) {
        return this.bookRepository.getOrThrowById(id);
    }

    @PostMapping
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public BookEntity createBook(@Valid @RequestBody CreateBookRequest request) {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setId(request.getPhysicalId());
        bookEntity.setName(request.getName());
        bookEntity.setRelease(this.releaseRepository.getOrThrowById(request.getReleaseId()));

        return this.bookRepository.save(bookEntity);
    }

    @PutMapping("/{id}")
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public BookEntity updateBook(@PathVariable String id, @Valid @RequestBody UpdateBookRequest request) {
        return this.bookRepository
            .findById(id)
            .map(bookEntity -> {
                bookEntity.setName(request.getName());
                bookEntity.setRelease(this.releaseRepository.getOrThrowById(request.getReleaseId()));
                return this.bookRepository.save(bookEntity);
            })
            .orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public void deleteById(@PathVariable String id) {
        this.bookRepository.deleteById(id);
    }
}

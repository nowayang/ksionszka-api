package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateBookRequest;
import com.github.Ksionzka.controller.dto.UpdateBookRequest;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController implements BaseController<BookEntity, String> {
    private final BookRepository bookRepository;
    private final ReleaseRepository releaseRepository;

    @Override
    @GetMapping
    @Transactional(readOnly = true)
    public Page<BookEntity> findAll(Pageable pageable, @PathVariable String search) {
        final String searchTerm = this.getSearchTerm(search);
        return this.bookRepository.findAll(
            (Specification<BookEntity>)
                (root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("id")), searchTerm),
                    cb.like(cb.lower(root.get("release").get("id")), searchTerm),
                    cb.like(cb.lower(root.get("release").get("publisher")), searchTerm),
                    cb.like(cb.lower(root.get("release").get("author")), searchTerm),
                    cb.like(cb.lower(root.get("release").get("genre")), searchTerm)
                ),
            pageable
        );
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public BookEntity getById(@PathVariable String id) {
        return this.bookRepository.getOrThrowById(id);
    }

    @PostMapping
    @Transactional
    public BookEntity createBook(@RequestBody CreateBookRequest request) {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setId(request.getPhysicalId());
        bookEntity.setRelease(this.releaseRepository.getOrThrowById(request.getReleaseId()));

        return this.bookRepository.save(bookEntity);
    }

    @PutMapping("/{id}")
    @Transactional
    public BookEntity updateBook(@PathVariable String id, @RequestBody UpdateBookRequest request) {
        return this.bookRepository
            .findById(id)
            .map(bookEntity -> {
                bookEntity.setRelease(this.releaseRepository.getOrThrowById(request.getReleaseId()));
                return this.bookRepository.save(bookEntity);
            })
            .orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.bookRepository.deleteById(id);
    }
}

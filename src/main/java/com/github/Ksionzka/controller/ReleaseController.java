package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateReleaseRequest;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
public class ReleaseController implements BaseController<ReleaseEntity, String> {
    private final ReleaseRepository releaseRepository;

    @Override
    @GetMapping()
    @Transactional(readOnly = true)
    public Page<ReleaseEntity> findAll(Pageable pageable, @RequestParam String search) {
        final String searchTerm = this.getSearchTerm(search);
        return this.releaseRepository.findAll(
            (Specification<ReleaseEntity>)
                (root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("id")), searchTerm),
                    cb.like(cb.lower(root.get("publisher")), searchTerm),
                    cb.like(cb.lower(root.get("author")), searchTerm),
                    cb.like(cb.lower(root.get("genre")), searchTerm)
                ),
            pageable
        );
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ReleaseEntity getById(@PathVariable String id) {
        return this.releaseRepository.getOrThrowById(id);
    }

    @PostMapping
    @Transactional
    public ReleaseEntity createRelease(@RequestBody CreateReleaseRequest request) {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        BeanUtils.copyProperties(request, releaseEntity);
        return this.releaseRepository.save(releaseEntity);
    }

    @PutMapping("/{id}")
    @Transactional
    public ReleaseEntity updateRelease(@PathVariable String id, @RequestBody CreateReleaseRequest request) {
        ReleaseEntity releaseEntity = this.getById(id);
        BeanUtils.copyProperties(request, releaseEntity);
        return this.releaseRepository.save(releaseEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable String id) {
        this.releaseRepository.deleteById(id);
    }
}

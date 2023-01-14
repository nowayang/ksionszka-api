package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.CreateReleaseRequest;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
public class ReleaseController implements BaseController<ReleaseEntity, String> {
    private final ReleaseRepository releaseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @GetMapping()
    @Transactional(readOnly = true)
    public Page<ReleaseEntity> findAll(Pageable pageable, @RequestParam(required = false) String search) {
        final String searchTerm = this.getSearchTerm(search);

        Specification<ReleaseEntity> specification = Specification.where(null);

        if (Strings.isNotBlank(search)) {
            specification = specification.and((root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("id")), searchTerm),
                cb.like(cb.lower(root.get("title")), searchTerm),
                cb.like(cb.lower(root.get("publisher")), searchTerm),
                cb.like(cb.lower(root.get("author")), searchTerm),
                cb.like(cb.lower(root.get("genre").as(String.class)), searchTerm)
            ));
        }

        return this.releaseRepository.findAll(specification, pageable);
    }

    @GetMapping("/top")
    public Page<ReleaseEntity> findAll(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ReleaseEntity> query = criteriaBuilder.createQuery(ReleaseEntity.class);
        Root<LoanEntity> root = query.from(LoanEntity.class);

        Expression<Long> count = criteriaBuilder.count(root.get("book").get("release").get("id"));

        query.select(root.get("book").get("release"));
        query.groupBy(root.get("book").get("release").get("id"));
        query.orderBy(criteriaBuilder.desc(count));

        TypedQuery<ReleaseEntity> typedQuery = this.entityManager.createQuery(query);
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(typedQuery.getResultList());
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ReleaseEntity getById(@PathVariable String id) {
        return this.releaseRepository.getOrThrowById(id);
    }

    @PostMapping
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public ReleaseEntity createRelease(@Valid @RequestBody CreateReleaseRequest request) {
        if (this.releaseRepository.existsById(request.getId())) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Release ID exists");
        }

        ReleaseEntity releaseEntity = new ReleaseEntity();
        BeanUtils.copyProperties(request, releaseEntity);
        return this.releaseRepository.save(releaseEntity);
    }

    @PutMapping("/{id}")
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public ReleaseEntity updateRelease(@PathVariable String id, @Valid @RequestBody CreateReleaseRequest request) {
        ReleaseEntity releaseEntity = this.getById(id);

        if (!Objects.equals(request.getId(), releaseEntity.getId()) && this.releaseRepository.existsById(request.getId())) {
            throw RestException.of(HttpStatus.BAD_REQUEST, "Release ID exists");
        }

        BeanUtils.copyProperties(request, releaseEntity);
        return this.releaseRepository.save(releaseEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    @Secured("ROLE_LIBRARIAN")
    public void deleteById(@PathVariable String id) {
        this.releaseRepository.deleteById(id);
    }
}

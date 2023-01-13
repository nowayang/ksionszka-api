package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.exception.ErrorMessage;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.specification.BookSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.HttpStatus;

import java.lang.reflect.ParameterizedType;

@NoRepositoryBean
public interface BaseRepository<T, K> extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {

    default T getOrThrowById(K k) {
        return this.findById(k)
            .orElseThrow(() -> RestException.of(
                HttpStatus.NOT_FOUND,
                String.format("%s %s", this.getTypeReadableName(), ErrorMessage.NOT_FOUND.readableString())
            ));
    }

    default boolean exists(K k, Specification<T> specification) {
        return this.exists((root, cq, cb) -> cb.and(
            cb.equal(root.get("id"), k),
            specification.toPredicate(root, cq, cb))
        );
    }

    String getTypeReadableName();
}

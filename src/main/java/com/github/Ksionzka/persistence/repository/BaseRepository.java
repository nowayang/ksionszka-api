package com.github.Ksionzka.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, K> extends JpaRepository<T, K>,
    JpaSpecificationExecutor<T> {

    default T getOrThrowById(K k) {
        return this.findById(k).orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }
}

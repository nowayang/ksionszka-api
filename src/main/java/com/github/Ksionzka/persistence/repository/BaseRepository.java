package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.ReservationEntity;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, K> extends JpaRepository<T, K>,
    JpaSpecificationExecutor<T> {

    default T getOrThrowById(K k) {
        return this.findById(k).orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }
}

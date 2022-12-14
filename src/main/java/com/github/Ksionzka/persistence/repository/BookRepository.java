package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<BookEntity, String>,
    JpaSpecificationExecutor<BookEntity> {
}

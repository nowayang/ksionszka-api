package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, String>,
    JpaSpecificationExecutor<ReleaseEntity> {
}

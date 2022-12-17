package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationRepository extends BaseRepository<ReservationEntity, String> {
}

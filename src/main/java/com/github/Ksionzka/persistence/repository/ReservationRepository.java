package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.ReservationEntity;

public interface ReservationRepository extends BaseRepository<ReservationEntity, Long> {

    @Override
    default String getTypeReadableName() {
        return "Reservation";
    }
}

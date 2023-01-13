package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.ReleaseEntity;

public interface ReleaseRepository extends BaseRepository<ReleaseEntity, String> {

    @Override
    default String getTypeReadableName() {
        return "Release";
    }
}

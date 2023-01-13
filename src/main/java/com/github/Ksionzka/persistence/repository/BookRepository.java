package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.persistence.entity.BookEntity;

public interface BookRepository extends BaseRepository<BookEntity, String> {
    @Override
    default String getTypeReadableName() {
        return "Book";
    }
}

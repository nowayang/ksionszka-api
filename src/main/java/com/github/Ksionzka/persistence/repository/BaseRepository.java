package com.github.Ksionzka.persistence.repository;

import com.github.Ksionzka.exception.ErrorMessage;
import com.github.Ksionzka.exception.RestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.HttpStatus;

import java.lang.reflect.ParameterizedType;

@NoRepositoryBean
public interface BaseRepository<T, K> extends JpaRepository<T, K>,
    JpaSpecificationExecutor<T> {

    default T getOrThrowById(K k) {
        return this.findById(k)
            .orElseThrow(() -> RestException.of(
                HttpStatus.NOT_FOUND,
                String.format("%s %s", this.getType().getName(), ErrorMessage.NOT_FOUND)
            ));
    }

    default Class<T> getType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}

package com.github.Ksionzka.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity

@NoArgsConstructor
@Getter
@Setter
public class BookEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "release_id", nullable = false)
    private ReleaseEntity release;
}

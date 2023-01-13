package com.github.Ksionzka.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BookEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "varchar(255) not null")
    private String number;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private ReleaseEntity release;
}

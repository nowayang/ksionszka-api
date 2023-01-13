package com.github.Ksionzka.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity

@NoArgsConstructor
@Getter
@Setter
public class ReleaseEntity {

    @Id
    private String id;

    @Column(columnDefinition = "varchar(255) not null")
    private String publisher;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Formula("extract(year from release_date)")
    private Long releaseYear;

    @Column(columnDefinition = "varchar(255) not null")
    private String author;

    @Column(columnDefinition = "varchar(255) not null")
    private String language;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;
}

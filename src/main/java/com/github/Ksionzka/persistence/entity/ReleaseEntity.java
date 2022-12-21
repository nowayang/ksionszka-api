package com.github.Ksionzka.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private ZonedDateTime date;

    @Column(columnDefinition = "varchar(255) not null")
    private String author;

    @Column(columnDefinition = "varchar(255) not null")
    private String language;

    @Column(columnDefinition = "varchar(255) not null")
    private String genre;
}

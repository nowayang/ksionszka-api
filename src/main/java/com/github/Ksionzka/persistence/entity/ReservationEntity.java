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
public class ReservationEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime creationDate;

    @Column(nullable = false)
    private ZonedDateTime reservationDate;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "book_id", nullable = false)
    private BookEntity book;
}

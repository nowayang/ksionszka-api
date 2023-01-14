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
public class LoanEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime loanDate;

    @Column(nullable = false)
    private ZonedDateTime returnDate;

    @Column
    private ZonedDateTime actualReturnDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private BookEntity book;

    @Column
    private ZonedDateTime requestedReturnDateExtensionAt;

    @Column(nullable = false, columnDefinition = "bool default false")
    private boolean notificationSent;
}

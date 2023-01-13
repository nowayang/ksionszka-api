package com.github.Ksionzka.persistence.specification;

import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.awt.print.Book;

@UtilityClass
public class BookSpecifications {

    public static Specification<BookEntity> isLoaned() {
        return (root, cq, cb) -> {
            Subquery<LoanEntity> bookLoanQuery = cq.subquery(LoanEntity.class);
            Root<LoanEntity> bookLoanRoot = bookLoanQuery.from(LoanEntity.class);

            bookLoanQuery.select(bookLoanRoot.get("id"))
                .where(cb.and(
                    cb.equal(bookLoanRoot.get("book").get("id"), root.get("id")),
                    cb.isNull(bookLoanRoot.get("actualReturnDate"))
                ));

            return cb.exists(bookLoanQuery);
        };
    }

    public static Specification<BookEntity> isReserved() {
        return (root, cq, cb) -> {
            Subquery<ReservationEntity> bookReservationQuery = cq.subquery(ReservationEntity.class);
            Root<ReservationEntity> bookReservationRoot = bookReservationQuery.from(ReservationEntity.class);

            bookReservationQuery.select(bookReservationRoot.get("id"))
                .where(cb.and(
                    cb.equal(bookReservationRoot.get("book").get("id"), root.get("id"))
                ));

            return cb.exists(bookReservationQuery);
        };
    }
}

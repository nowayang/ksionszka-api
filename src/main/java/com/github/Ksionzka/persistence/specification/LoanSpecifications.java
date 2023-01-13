package com.github.Ksionzka.persistence.specification;

import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.LoanEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.ZonedDateTime;

@UtilityClass
public class LoanSpecifications {

    public static Specification<LoanEntity> isDelayed() {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("loanDate"), ZonedDateTime.now());
    }
}

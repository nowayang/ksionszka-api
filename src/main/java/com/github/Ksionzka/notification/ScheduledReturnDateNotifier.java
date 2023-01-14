package com.github.Ksionzka.notification;

import com.github.Ksionzka.persistence.repository.LoanRepository;
import com.github.Ksionzka.persistence.specification.LoanSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledReturnDateNotifier {
    private final ApplicationEventPublisher publisher;
    private final LoanRepository loanRepository;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyUsers() {
        this.loanRepository.findAll(
                Specification.not(LoanSpecifications.isReturned())
                    .and(Specification.not(LoanSpecifications.isDelayed()))
                    .and((root, cq, cb) -> cb.isFalse(root.get("notificationSent")))
                    .and((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("returnDate"), ZonedDateTime.now().plusDays(1)))
            )
            .stream()
            .peek(loanEntity -> loanEntity.setNotificationSent(true))
            .map(ReturnDateNotification::new)
            .forEach(this::sendEvent);
    }

    @Async
    void sendEvent(ReturnDateNotification returnDateNotification) {
        this.publisher.publishEvent(returnDateNotification);
    }
}

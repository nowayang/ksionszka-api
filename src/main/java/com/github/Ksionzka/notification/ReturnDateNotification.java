package com.github.Ksionzka.notification;

import com.github.Ksionzka.persistence.entity.LoanEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReturnDateNotification extends ApplicationEvent {
    private final LoanEntity loanEntity;

    public ReturnDateNotification(LoanEntity loanEntity) {
        super(loanEntity);
        this.loanEntity = loanEntity;
    }
}

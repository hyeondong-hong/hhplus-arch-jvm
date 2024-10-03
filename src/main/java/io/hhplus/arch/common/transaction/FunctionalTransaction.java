package io.hhplus.arch.common.transaction;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public final class FunctionalTransaction {
    private final PlatformTransactionManager transactionManager;

    public FunctionalTransaction(
            PlatformTransactionManager transactionManager
    ) {
        this.transactionManager = transactionManager;
    }

    public <T> T perform(
            @NonNull TransactionItem<T> transactionItem
    ) {
        return transactionItem.execute(transactionManager, transactionItem);
    }
}

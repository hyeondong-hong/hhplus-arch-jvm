package io.hhplus.arch.common.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

public class TransactionHandler {

    private final PlatformTransactionManager transactionManager;
    private final TransactionStatus status;

    private boolean isClosed = false;

    public TransactionHandler(PlatformTransactionManager transactionManager, TransactionStatus status) {
        this.transactionManager = transactionManager;
        this.status = status;
    }

    public void commit() {
        transactionManager.commit(status);
        isClosed = true;
    }

    public void rollback() {
        transactionManager.rollback(status);
        isClosed = true;
    }

    public boolean isClosed() {
        return isClosed;
    }
}

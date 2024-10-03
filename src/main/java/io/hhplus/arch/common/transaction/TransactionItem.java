package io.hhplus.arch.common.transaction;

import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;

public final class TransactionItem<T> {
    private final boolean readOnly;
    private final int isolationLevel;
    private final Function<TransactionHandler, ?> func;
    private final int propagationBehavior;

    public TransactionItem(
            @NonNull Function<TransactionHandler, ?> func
    ) {
        this(
                false,
                TransactionDefinition.ISOLATION_DEFAULT,
                TransactionDefinition.PROPAGATION_REQUIRED,
                func
        );
    }

    public TransactionItem(
            int isolationLevel,
            @NonNull Function<TransactionHandler, ?> func
    ) {
        this(
                false,
                isolationLevel,
                TransactionDefinition.PROPAGATION_REQUIRED,
                func
        );
    }

    public TransactionItem(
            boolean readOnly,
            int isolationLevel,
            @NonNull Function<TransactionHandler, ?> func
    ) {
        this(
                readOnly,
                isolationLevel,
                TransactionDefinition.PROPAGATION_REQUIRED,
                func
        );
    }

    public TransactionItem(
            boolean readOnly,
            int isolationLevel,
            int propagationBehavior,
            @NonNull Function<TransactionHandler, ?> func
    ) {
        this.readOnly = readOnly;
        this.isolationLevel = isolationLevel;
        this.propagationBehavior = propagationBehavior;
        this.func = func;
    }

    public T execute(
            @NonNull PlatformTransactionManager transactionManager,
            @NonNull TransactionItem<T> firstTransactionItem
    ) {
        Deque<TransactionItem<T>> itemStack = new ArrayDeque<>();
        Deque<TransactionStatus> statusStack = new ArrayDeque<>();
        try {
            itemStack.push(firstTransactionItem);
            Object result;
            do {
                TransactionItem<T> current = itemStack.pop();

                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setReadOnly(current.readOnly);
                def.setIsolationLevel(current.isolationLevel);
                def.setPropagationBehavior(current.propagationBehavior);

                TransactionStatus status = transactionManager.getTransaction(def);

                TransactionHandler handler = new TransactionHandler(transactionManager, status);
                result = current.func.apply(handler);
                if (!handler.isClosed()) {
                    statusStack.push(status);
                }

                if (result instanceof TransactionItem<?>) {
                    itemStack.push((TransactionItem<T>) result);
                }
            } while (!itemStack.isEmpty());

            while (!statusStack.isEmpty()) {
                transactionManager.commit(statusStack.pop());
            }

            return (T) result;
        } catch (Exception e) {
            while (!statusStack.isEmpty()) {
                transactionManager.rollback(statusStack.pop());
            }
            throw e;
        }
    }
}

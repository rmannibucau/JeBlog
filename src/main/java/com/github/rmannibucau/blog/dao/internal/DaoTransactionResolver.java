package com.github.rmannibucau.blog.dao.internal;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.concurrent.Callable;

@Singleton
@Lock(LockType.READ)
public class DaoTransactionResolver implements Serializable {
    public Object run(final Callable<Object> r) {
        try {
            return r.call();
        } catch (final Exception e) {
            if (RuntimeException.class.isInstance(e)) {
                throw RuntimeException.class.cast(e);
            }
            throw new TransactionException(e);
        }
    }

    public static class TransactionException extends RuntimeException {
        public TransactionException(final Exception e) {
            super(e);
        }
    }
}

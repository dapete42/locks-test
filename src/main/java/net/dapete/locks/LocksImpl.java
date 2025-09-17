package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

class LocksImpl<K, L extends Lock> extends AbstractLocks<K, L> implements Locks<K, L> {

    LocksImpl(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    @Override
    public final L lock(K key) {
        final var lock = get(key);
        lock.lock();
        return lock;
    }

}

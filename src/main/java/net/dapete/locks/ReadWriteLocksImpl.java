package net.dapete.locks;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

class ReadWriteLocksImpl<K, L extends ReadWriteLock> extends AbstractLocks<K, L> implements ReadWriteLocks<K, L> {

    ReadWriteLocksImpl(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    @Override
    public final L readLock(K key) {
        final var lock = get(key);
        lock.readLock().lock();
        return lock;
    }

    @Override
    public final L writeLock(K key) {
        final var lock = get(key);
        lock.writeLock().lock();
        return lock;
    }

}

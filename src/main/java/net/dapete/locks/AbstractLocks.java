package net.dapete.locks;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Abstract base implementation of key-based locking.
 *
 * @param <K> type of key
 * @param <L> type of lock
 */
abstract class AbstractLocks<K, L> {

    private final Lock instanceLock = new ReentrantLock();

    private final Map<Object, LockReference<K, L>> lockReferenceMap = new HashMap<>();

    private final ReferenceQueue<L> lockReferenceQueue = new ReferenceQueue<>();

    private final Supplier<L> lockSupplier;

    protected AbstractLocks(Supplier<L> lockSupplier) {
        this.lockSupplier = lockSupplier;
    }

    /**
     * Returns a new lock for the supplied key.
     *
     * @param key key
     * @return lock
     */
    public L get(K key) {
        processQueue();
        instanceLock.lock();
        try {
            final var lockReference = lockReferenceMap.get(key);
            final var lock = lockReference == null ? null : lockReference.get();
            if (lock == null) {
                final var newLock = lockSupplier.get();
                lockReferenceMap.put(key, new LockReference<>(key, newLock, lockReferenceQueue));
                return newLock;
            } else {
                return lock;
            }
        } finally {
            instanceLock.unlock();
        }
    }

    /**
     * Returns the current number of locks managed by this instance.
     *
     * @return number of locks
     */
    public int size() {
        processQueue();
        instanceLock.lock();
        try {
            return lockReferenceMap.size();
        } finally {
            instanceLock.unlock();
        }
    }

    /**
     * Removes all locks that have been marked as unreachable by the garbage collector.
     */
    private void processQueue() {
        instanceLock.lock();
        try {
            LockReference<?, ?> lockReference;
            while ((lockReference = (LockReference<?, ?>) lockReferenceQueue.poll()) != null) {
                lockReferenceMap.remove(lockReference.getKey());
            }
        } finally {
            instanceLock.unlock();
        }
    }

}

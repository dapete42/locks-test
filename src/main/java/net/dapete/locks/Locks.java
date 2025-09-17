package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Key-based locking with implementations of {@link Lock}.
 *
 * @param <K> type of key
 * @param <L> type of {@link Lock}
 */
public interface Locks<K, L extends Lock> {

    /**
     * Return an instance using {@link Lock} implementations created by the specified {@code lockSupplier}.
     *
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link Lock})
     * @param <K>          type of key
     * @param <L>          type of {@link Lock}
     * @return instance using {@code Lock} implementations created by the specified {@code lockSupplier}
     */
    static <K, L extends Lock> Locks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new LocksImpl<>(lockSupplier);
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock}.
     *
     * @param <K> type of key
     * @return {@code ReentrantLocks} instance
     */
    static <K> ReentrantLocks<K> reentrant() {
        return new ReentrantLocks<>();
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock}.
     *
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantLocks} instance
     */
    static <K> ReentrantLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant();
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock} with the given fairness policy.
     *
     * @param fair {@code true} if the locks should use a fair ordering policy (see {@link ReentrantLock#ReentrantLock(boolean)})
     * @param <K>  type of key
     * @return {@code ReentrantLocks} instance
     * @since 1.2.0
     */
    static <K> ReentrantLocks<K> reentrant(boolean fair) {
        return new ReentrantLocks<>(fair);
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock} with the given fairness policy.
     *
     * @param fair     {@code true} if the locks should use a fair ordering policy (see {@link ReentrantLock#ReentrantLock(boolean)})
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantLocks} instance
     * @since 1.2.0
     */
    static <K> ReentrantLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant(fair);
    }

    /**
     * Returns a lock for the supplied key. There will be at most one lock per key at any given time.
     *
     * @param key key
     * @return lock
     */
    L get(K key);

    /**
     * Return a {@code Lock} already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already locked lock
     */
    L lock(K key);

    /**
     * Returns the current number of locks managed by this instance.
     *
     * @return number of locks
     */
    int size();

}

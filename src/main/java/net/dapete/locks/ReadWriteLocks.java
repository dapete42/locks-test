package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Key-based locking with implementations of {@link ReadWriteLock}.
 *
 * @param <K> type of key
 * @param <L> type of ReadWriteLock
 */
public interface ReadWriteLocks<K, L extends ReadWriteLock> {

    /**
     * Return an instance using {@link ReadWriteLock} implementations created by the specified {@code lockSupplier}.
     *
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link ReadWriteLock})
     * @param <K>          type of key
     * @param <L>          type of {@link Lock}
     * @return instance using {@code ReadWriteLock} implementations created by the specified {@code lockSupplier}
     */
    static <K, L extends ReadWriteLock> ReadWriteLocks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new ReadWriteLocksImpl<>(lockSupplier);
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock}.
     *
     * @param <K> type of key
     * @return {@code ReentrantReadWriteLocks} instance
     */
    static <K> ReentrantReadWriteLocks<K> reentrant() {
        return new ReentrantReadWriteLocks<>();
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock}.
     *
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantReadWriteLocks} instance
     */
    static <K> ReentrantReadWriteLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant();
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock} with the given fairness policy.
     *
     * @param fair {@code true} if the locks should use a fair ordering policy (see {@link ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)})
     * @param <K>  type of key
     * @return {@code ReentrantReadWriteLocks} instance
     * @since 1.2.0
     */
    static <K> ReentrantReadWriteLocks<K> reentrant(boolean fair) {
        return new ReentrantReadWriteLocks<>(fair);
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock} with the given fairness policy.
     *
     * @param fair     {@code true} if the locks should use a fair ordering policy (see {@link ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)})
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantReadWriteLocks} instance
     * @since 1.2.0
     */
    static <K> ReentrantReadWriteLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
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
     * Return a {@code ReadWriteLock} with its {@link ReadWriteLock#readLock()} already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already read locked lock
     */
    L readLock(K key);

    /**
     * Return a {@code ReadWriteLock} with its {@link ReadWriteLock#writeLock()} already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already write locked lock
     */
    L writeLock(K key);

    /**
     * Returns the current number of locks managed by this instance.
     *
     * @return number of locks
     */
    int size();

}

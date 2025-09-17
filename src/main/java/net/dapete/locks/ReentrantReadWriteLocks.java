package net.dapete.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Key-based locking using instances of {@link ReentrantReadWriteLock}.
 * <p>
 * Instances can be created using {@link ReadWriteLocks#reentrant()}, {@link ReadWriteLocks#reentrant(Class)}, {@link ReadWriteLocks#reentrant(boolean)} and
 * {@link ReadWriteLocks#reentrant(boolean, Class)}.
 *
 * @param <K> type of key
 */
public final class ReentrantReadWriteLocks<K> extends ReadWriteLocksImpl<K, ReentrantReadWriteLock> {

    ReentrantReadWriteLocks() {
        super(ReentrantReadWriteLock::new);
    }

    ReentrantReadWriteLocks(boolean fair) {
        super(() -> new ReentrantReadWriteLock(fair));
    }

}

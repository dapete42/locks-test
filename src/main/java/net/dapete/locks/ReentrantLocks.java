package net.dapete.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Key-based locking using instances of {@link ReentrantLock}.
 * <p>
 * Instances can be created using {@link Locks#reentrant()}, {@link Locks#reentrant(Class)}, {@link Locks#reentrant(boolean)} and
 * {@link Locks#reentrant(boolean, Class)}.
 *
 * @param <K> type of key
 */
public final class ReentrantLocks<K> extends LocksImpl<K, ReentrantLock> {

    ReentrantLocks() {
        super(ReentrantLock::new);
    }

    ReentrantLocks(boolean fair) {
        super(() -> new ReentrantLock(fair));
    }

}

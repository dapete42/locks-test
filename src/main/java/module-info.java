import org.jspecify.annotations.NullMarked;

/**
 * <p>
 *     This module contains classes for key-based locking, as in a way to obtain instances of {@link java.util.concurrent.locks.Lock Lock} or
 *     {@link java.util.concurrent.locks.ReadWriteLock ReadWriteLock} which are identified by a key.
 * </p>
 *
 * @see net.dapete.locks documentation for package net.dapete.locks
 * @since 1.1.0
 */
@NullMarked
module net.dapete.locks {
    requires org.jspecify;
    exports net.dapete.locks;
}

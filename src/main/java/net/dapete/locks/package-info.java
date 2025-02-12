/**
 * <p>
 * This package contains classes for key-based locking, as in a way to obtain {@link java.util.concurrent.locks.Lock Lock}s
 * or {@link java.util.concurrent.locks.ReadWriteLock ReadWriteLock}s which are identified by a key. These locks are guaranteed to by different for each key and
 * will be held as long as they are referenced.
 * </p>
 * <p>
 * The possible ways to obtain these are:
 * </p>
 * <ul>
 * <li>{@link net.dapete.locks.Locks#reentrant() Locks.reentrant()}</li>
 * <li>{@link net.dapete.locks.Locks#reentrant(java.lang.Class) Locks.reentrant(Class)}</li>
 * <li>{@link net.dapete.locks.Locks#withSupplier(java.util.function.Supplier) Locks.withSupplier(Supplier)}</li>
 * <li>{@link net.dapete.locks.ReadWriteLocks#reentrant() ReadWriteLocks.reentrant()}</li>
 * <li>{@link net.dapete.locks.ReadWriteLocks#reentrant(java.lang.Class) ReadWriteLocks.reentrant(Class)}</li>
 * <li>{@link net.dapete.locks.ReadWriteLocks#withSupplier(java.util.function.Supplier) ReadWriteLocks.withSupplier(Supplier)}</li>
 * </ul>
 * <p>
 *     All implementations use generics for the type of the key as well as the type of lock, if this is not fixed (as with
 *     {@link net.dapete.locks.ReentrantLocks} and {@link net.dapete.locks.ReentrantReadWriteLocks}).
 * <p>
 *     The parameter on the {@code reentrant(Class)} methods is the {@link java.lang.Class} of the key type; this is a shortcut for readability, if the
 *     compiler cannot automatically detect it, e.g. {@code var x = Locks.reentrant(String.class)}.
 * </p>
 * <p>
 *     The {@code withSupplier(Supplier)} methods allow for any implementation of {@code Lock} or {@code ReentrantLock} to be used; just use the constructor
 *     as the Supplier, e.g. {@code Locks.withSupplier(SuperEpicLock::new)}.
 * </p>
 */
@NullMarked
package net.dapete.locks;

import org.jspecify.annotations.NullMarked;

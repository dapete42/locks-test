package net.dapete.locks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadWriteLocksTest {

    @Test
    void testLocking() {
        final var readWriteLocks = ReadWriteLocks.reentrant(Integer.class);

        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);

        final var readWriteLock = readWriteLocks.readLock(1);
        try {

            Runnable runnable = () -> {
                threadHasStarted.set(true);
                final var readWriteLock2 = readWriteLocks.writeLock(1);
                assertSame(readWriteLock, readWriteLock2);
                try {
                    threadHasLocked.set(true);
                } finally {
                    readWriteLock2.writeLock().unlock();
                }
            };
            new Thread(runnable).start();
            await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasStarted);
            assertFalse(threadHasLocked.get());

        } finally {
            readWriteLock.readLock().unlock();
        }

        await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasLocked);
    }

    @Test
    void withSupplier() {
        @SuppressWarnings("unchecked") final Supplier<ReentrantReadWriteLock> lockSupplier = mock(Supplier.class);
        when(lockSupplier.get()).thenAnswer(invocation -> new ReentrantReadWriteLock());

        final var locks = ReadWriteLocks.withSupplier(lockSupplier);

        verifyNoInteractions(lockSupplier);

        locks.readLock(1).readLock().unlock();

        verify(lockSupplier).get();
    }

    @Test
    void reentrant() {
        final var locks = ReadWriteLocks.reentrant(Integer.class);

        final var lock = locks.readLock(1);
        try {
            assertFalse(lock.isFair());
        } finally {
            lock.readLock().unlock();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void reentrant(boolean fair) {
        final var locks = ReadWriteLocks.reentrant(fair, Integer.class);

        final var lock = locks.readLock(1);
        try {
            assertEquals(fair, lock.isFair());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Test
    void readLock() {
        final var locks = ReadWriteLocks.reentrant(Integer.class);

        final var lock = locks.readLock(1);

        assertEquals(1, lock.getReadLockCount());

        lock.readLock().unlock();
    }

    @Test
    void writeLock() {
        final var locks = ReadWriteLocks.reentrant(Integer.class);

        final var lock = locks.writeLock(1);

        assertTrue(lock.isWriteLocked());

        lock.writeLock().unlock();
    }

}

package net.dapete.locks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocksTest {

    @Test
    void testLocking() {
        final var locks = Locks.reentrant(Integer.class);

        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);

        final var lock = locks.lock(1);
        try {

            // lock the lock in another thread and wait until it's running
            Runnable runnable = () -> {
                threadHasStarted.set(true);
                final var lock2 = locks.lock(1);
                assertSame(lock, lock2);
                try {
                    threadHasLocked.set(true);
                } finally {
                    lock2.unlock();
                }
            };
            new Thread(runnable).start();
            await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasStarted);
            assertFalse(threadHasLocked.get());

        } finally {
            lock.unlock();
        }

        await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasLocked);
    }

    @Test
    void withSupplier() {
        @SuppressWarnings("unchecked") final Supplier<ReentrantLock> lockSupplier = mock(Supplier.class);
        when(lockSupplier.get()).thenAnswer(invocation -> new ReentrantLock());

        final var locks = Locks.withSupplier(lockSupplier);
        verifyNoInteractions(lockSupplier);

        locks.lock(1).unlock();

        verify(lockSupplier).get();
    }

    @Test
    void reentrant() {
        final var locks = Locks.reentrant(Integer.class);

        final var lock = locks.lock(1);
        try {
            assertFalse(lock.isFair());
        } finally {
            lock.unlock();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void reentrant(boolean fair) {
        final var locks = Locks.reentrant(fair, Integer.class);

        final var lock = locks.lock(1);
        try {
            assertEquals(fair, lock.isFair());
        } finally {
            lock.unlock();
        }
    }

    @Test
    void lock() {
        final var locks = Locks.reentrant(Integer.class);

        final var lock = locks.lock(1);

        assertTrue(lock.isLocked());

        lock.unlock();
    }

}

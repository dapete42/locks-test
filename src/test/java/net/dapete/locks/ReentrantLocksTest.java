package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReentrantLocksTest {

    @Test
    void testLocksAreReleasedWhenUnused() throws Exception {
        final var locks = Locks.reentrant(Integer.class);

        var lock = locks.lock(1);
        lock.unlock();

        int size = locks.size();
        assertEquals(1, size);

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, even
         * {@code System.gc()} is just a suggestion, but this seems to work.
         */
        lock = null;
        for (int i = 300; i > 0 && size > 0; i--) {
            System.gc();
            Thread.sleep(100);
            size = locks.size();
        }
        assertEquals(0, size);
    }

    @Test
    void testLocking() throws Exception {
        final var locks = Locks.reentrant(Integer.class);

        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);

        var lock = locks.lock(1);
        try {

            Runnable runnable = () -> {
                threadHasStarted.set(true);
                final var lock2 = locks.lock(1);
                try {
                    threadHasLocked.set(true);
                } finally {
                    lock2.unlock();
                }
            };
            new Thread(runnable).start();

            while (!threadHasStarted.get()) {
                Thread.yield();
            }

            TimeUnit.SECONDS.sleep(1);

            assertFalse(threadHasLocked.get());

        } finally {
            lock.unlock();
        }

        TimeUnit.SECONDS.sleep(1);

        assertTrue(threadHasLocked.get());
    }


}

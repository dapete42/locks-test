package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReentrantReadWriteLocksTest {

    @Test
    void testReadLocksAreReleasedWhenUnused() throws Exception {
        final var readWriteLocks = ReadWriteLocks.reentrant(Integer.class);

        var readWriteLock = readWriteLocks.readLock(1);
        readWriteLock.readLock().unlock();

        int size = readWriteLocks.size();
        assertEquals(1, size);

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, even
         * {@code System.gc()} is just a suggestion, but this seems to work.
         */
        readWriteLock = null;
        for (int i = 300; i > 0 && size > 0; i--) {
            // not guaranteed to do anything
            System.gc();
            Thread.sleep(100);
            size = readWriteLocks.size();
        }
        assertEquals(0, size);
    }

    @Test
    void testLocking() throws Exception {
        final var readWriteLocks = ReadWriteLocks.reentrant(Integer.class);

        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);

        var readWriteLock = readWriteLocks.readLock(1);
        try {

            Runnable runnable = () -> {
                threadHasStarted.set(true);
                final var readWriteLock2 = readWriteLocks.writeLock(1);
                try {
                    threadHasLocked.set(true);
                } finally {
                    readWriteLock2.writeLock().unlock();
                }
            };
            new Thread(runnable).start();

            while (!threadHasStarted.get()) {
                Thread.yield();
            }

            TimeUnit.SECONDS.sleep(1);

            assertFalse(threadHasLocked.get());

        } finally {
            readWriteLock.readLock().unlock();
        }

        TimeUnit.SECONDS.sleep(1);

        assertTrue(threadHasLocked.get());
    }


}

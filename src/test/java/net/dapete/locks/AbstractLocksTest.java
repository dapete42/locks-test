package net.dapete.locks;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class AbstractLocksTest {

    private static class TestAbstractLocks extends AbstractLocks<@NonNull Integer, @NonNull ReentrantLock> {

        private TestAbstractLocks() {
            super(ReentrantLock::new);
        }

        private void clearLockReference(Integer key) {
            final var lockReference = getLockReference(key);
            if (lockReference != null) {
                lockReference.clear();
            }
        }

    }

    @Test
    void testLocksAreReleasedWhenUnused() {
        final var locks = new TestAbstractLocks();

        locks.get(1);

        assertEquals(1, locks.size());

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, System.gc() is just a
         * suggestion, but this seems to work.
         */
        System.gc();
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 0);
    }

    @Test
    void get_createNewLockIfLockReferenceIsNull() {
        final var locks = new TestAbstractLocks();

        // get one lock and then clear the reference to it
        final var lock1 = locks.get(1);
        locks.clearLockReference(1);

        // this should not be null, but a different lock
        final var lock2 = locks.get(1);

        assertNotNull(lock2);
        assertNotSame(lock1, lock2);
    }

    @Test
    void get_differentForDifferentKeys() {
        final var locks = new TestAbstractLocks();

        assertNotSame(locks.get(1), locks.get(2));
    }

    @Test
    void get_identicalForIdenticalKey() {
        final var locks = new TestAbstractLocks();

        assertSame(locks.get(1), locks.get(1));
    }

    @Test
    void size() {
        final var locks = new TestAbstractLocks();

        // Initially the size should be 0
        assertEquals(0, locks.size(), "Initial size should be 0");

        // Add some locks
        final var lockList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lockList.add(locks.get(i));
        }

        // Size should reflect the number of locks
        assertEquals(5, locks.size(), "Size should match the number of locks");

        // Clear some references and force GC
        lockList.subList(0, 3).clear();
        System.gc();

        // Wait for size to decrease
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 2);

        // Clear remaining references
        lockList.clear();
        System.gc();

        // Wait for size to reach 0
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 0);
    }

}

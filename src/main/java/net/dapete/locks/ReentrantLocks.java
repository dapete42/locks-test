package net.dapete.locks;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLocks<K> extends Locks<K, ReentrantLock> {

    ReentrantLocks() {
        super(ReentrantLock::new);
    }

}

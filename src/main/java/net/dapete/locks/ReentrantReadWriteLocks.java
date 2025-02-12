package net.dapete.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLocks<K> extends ReadWriteLocks<K, ReentrantReadWriteLock> {

    ReentrantReadWriteLocks() {
        super(ReentrantReadWriteLock::new);
    }

}

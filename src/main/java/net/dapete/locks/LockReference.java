package net.dapete.locks;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class LockReference<K, L> extends WeakReference<L> {

    private final K key;

    LockReference(K key, L value, ReferenceQueue<? super L> referenceQueue) {
        super(value, referenceQueue);
        this.key = key;
    }

    K getKey() {
        return key;
    }

}

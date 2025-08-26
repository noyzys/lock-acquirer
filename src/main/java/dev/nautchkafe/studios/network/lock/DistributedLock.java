package dev.nautchkafe.studios.network.lock;

interface DistributedLock {

    boolean tryAcquire(final LockRequest request, final LockIdentifier lockId);
}

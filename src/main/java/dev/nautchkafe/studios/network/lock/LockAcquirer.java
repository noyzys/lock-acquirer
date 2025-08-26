package dev.nautchkafe.studios.network.lock;

@FunctionalInterface
interface LockAcquirer {

    boolean tryAcquire(final LockRequest request, final LockIdentifier lockId);
}

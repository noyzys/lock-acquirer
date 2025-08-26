package dev.nautchkafe.studios.network.lock;

@FunctionalInterface
interface LockReleaser {

    void release(final AcquiredLock lock);
}
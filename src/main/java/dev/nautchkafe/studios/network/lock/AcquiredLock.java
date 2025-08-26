package dev.nautchkafe.studios.network.lock;

record AcquiredLock(
        ResourceKey key,
        LockIdentifier lockId
) {
}

package dev.nautchkafe.studios.network.lock;

@FunctionalInterface
interface LockCriticalSection<RESULT> {

    RESULT execute(final AcquiredLock lock) throws Exception;
}
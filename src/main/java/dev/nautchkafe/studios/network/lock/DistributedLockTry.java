package dev.nautchkafe.studios.network.lock;

import java.util.function.Function;

interface DistributedLockTry {

    <RESULT> LockChoice<RESULT> tryExecute(final LockRequest request,
                                           final Function<? super AcquiredLock, ? extends RESULT> criticalSection);

    default LockChoice<Void> tryExecute(final LockRequest request,
                                        final Runnable criticalRunnable) {

        final Function<AcquiredLock, Void> functorMapper = lock -> { criticalRunnable.run();
            return null;
        };

        return tryExecute(request, functorMapper);
    }
}
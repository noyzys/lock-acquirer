package dev.nautchkafe.studios.network.lock;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

final class DistributedLockComposition implements DistributedLockTry {

    private final DistributedLock lock;
    private final Supplier<LockIdentifier> idGenerator;

    DistributedLockComposition(final DistributedLock lock,
                               final Supplier<LockIdentifier> idGenerator) {
        this.lock = lock;
        this.idGenerator = idGenerator;
    }

    DistributedLockComposition(final DistributedLock lock) {
        this(lock, () -> new LockIdentifier(UUID.randomUUID().toString()));
    }

    @Override
    public <RESULT> LockChoice<RESULT> tryExecute(final LockRequest request,
                                                  final Function<? super AcquiredLock, ? extends RESULT> criticalSection) {
        final LockIdentifier lockIdentifier = idGenerator.get();

        if (!lock.tryAcquire(request, lockIdentifier)) {
            return new LockChoice.LockNotAcquired<>();
        }

        final AcquiredLock acquiredLock = new AcquiredLock(
                request.key(),
                lockIdentifier);

        try {
            final RESULT result = criticalSection.apply(acquiredLock);
            return new LockChoice.Success<>(result);
        } catch (final Exception e) {
            return new LockChoice.Failure<>(e);
        }
    }
}

package dev.nautchkafe.studios.network.lock;

public record LockRequest(
        ResourceKey key,
        LockDuration duration
) {
}

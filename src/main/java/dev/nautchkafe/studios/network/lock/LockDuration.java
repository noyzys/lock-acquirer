package dev.nautchkafe.studios.network.lock;

import java.time.Duration;

record LockDuration(Duration value) {

    public static LockDuration of(final Duration duration) {
        return new LockDuration(duration);
    }

    public static LockDuration ofSeconds(final long seconds) {
        return new LockDuration(Duration.ofSeconds(seconds));
    }
}

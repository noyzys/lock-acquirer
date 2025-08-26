package dev.nautchkafe.studios.network.lock;

sealed interface LockChoice<RESULT> {

    record Success<RESULT>(RESULT value) implements LockChoice<RESULT> {
    }

    record Failure<RESULT>(Throwable cause) implements LockChoice<RESULT> {
    }

    record LockNotAcquired<RESULT>() implements LockChoice<RESULT> {
    }
}

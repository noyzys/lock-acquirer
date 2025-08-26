package dev.nautchkafe.studios.network.lock;

import java.util.UUID;

record LockIdentifier(String value) {

    public static LockIdentifier random() {
        return new LockIdentifier(UUID.randomUUID().toString());
    }
}

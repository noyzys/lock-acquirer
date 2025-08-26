package dev.nautchkafe.studios.network.session.identity;

import java.util.UUID;

public record PlayerIdentity(
        UUID playerId,
        String playerName,
        long joinTimestamp
) {
}

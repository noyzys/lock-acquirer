package dev.nautchkafe.studios.network.session.metric;

import java.util.UUID;

public record GameSession(
        UUID sessionId,
        String gameType,
        int playerCount,
        int durationSeconds,
        UUID winningPlayerId,
        long sessionEndTimestamp
) {
}

package dev.nautchkafe.studios.network.session.metric;

import java.util.UUID;

public record ServerMetric(
        UUID serverId,
        int currentPlayerCount,
        double serverLoad,
        long lastHeartbeat,
        boolean isHealthy
) {
}

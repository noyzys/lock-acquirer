package dev.nautchkafe.studios.network.session.contract;

import java.util.UUID;

public record TeleportRequest(
        UUID requestId,
        UUID playerId,
        UUID sourceServerId,
        UUID targetServerId,
        long requestTimestamp
) {
}

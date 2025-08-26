package dev.nautchkafe.studios.network.session;

import dev.nautchkafe.studios.network.session.contract.TeleportRequest;

@FunctionalInterface
interface TeleportExecutor {

    SessionChoice<String, TeleportRequest> executeTeleport(final TeleportRequest teleportRequest);
}

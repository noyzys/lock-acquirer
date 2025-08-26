package dev.nautchkafe.studios.network.session.contract;

import dev.nautchkafe.studios.network.session.identity.PlayerIdentity;
import dev.nautchkafe.studios.network.session.identity.ServerIdentity;

import java.util.UUID;

public record ConnectionContract(
        UUID connectionId,
        PlayerIdentity playerIdentity,
        ServerIdentity targetServer,
        String authenticationToken,
        long requestTimestamp
) {
}

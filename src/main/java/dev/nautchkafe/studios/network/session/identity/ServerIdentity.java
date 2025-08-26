package dev.nautchkafe.studios.network.session.identity;

import java.net.InetSocketAddress;
import java.util.UUID;

public record ServerIdentity(
        UUID serverId,
        String serverName,
        InetSocketAddress serverAdress,
        String instance,
        int maxPlayerCapacity
) {
}

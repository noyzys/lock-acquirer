package dev.nautchkafe.studios.network.session.selection;

import dev.nautchkafe.studios.network.session.identity.ServerIdentity;
import dev.nautchkafe.studios.network.session.metric.ServerMetric;
import dev.nautchkafe.studios.network.session.SessionChoice;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

final class RoundRobinSelector implements ServerSelector {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public SessionChoice<String, ServerIdentity> selectOptimalServer(
            final List<ServerIdentity> servers,
            final List<ServerMetric> metrics,
            final String instance) {

        final List<ServerIdentity> filteredServers = filterServersByGameType(servers, instance);
        return filteredServers.isEmpty()
                ? SessionChoice.failure("No servers available for instance : " + instance)
                : selectRoundRobinServer(filteredServers);

    }

    private List<ServerIdentity> filterServersByGameType(final List<ServerIdentity> servers, String instance) {
        return servers.stream()
                .filter(server -> server.serverName().equals(instance))
                .toList();
    }

    private SessionChoice<String, ServerIdentity> selectRoundRobinServer(final List<ServerIdentity> servers) {
        final int index = currentIndex.getAndUpdate(operand -> (operand + 1) % servers.size());
        return SessionChoice.success(servers.get(index));
    }
}

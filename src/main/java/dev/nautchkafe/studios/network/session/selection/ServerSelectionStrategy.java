package dev.nautchkafe.studios.network.session.selection;

import dev.nautchkafe.studios.network.session.identity.ServerIdentity;
import dev.nautchkafe.studios.network.session.metric.ServerMetric;
import dev.nautchkafe.studios.network.session.SessionChoice;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public final class ServerSelectionStrategy {

    private ServerSelectionStrategy() {
    }

    public static ServerSelector createRoundRobinSelector() {
        return new RoundRobinSelector();
    }

    public static ServerSelector createLeastConnectionSelector() {
        return ServerSelectionStrategy::findServerWithLeastConnections;
    }

    public static ServerSelector createLoadBasedSelector(final Function<ServerMetric, Double> loadCalculator) {
        return (servers, metrics, instance) ->
                findServerWithLowestLoad(servers, metrics, instance, loadCalculator);
    }

    private static List<ServerIdentity> filterServersByInstance(final List<ServerIdentity> servers,
                                                                final String instance) {
        return servers.stream()
                .filter(server -> server.serverName().equals(instance))
                .toList();
    }

    private static SessionChoice<String, Integer> serverPlayerCount(final List<ServerMetric> metrics,
                                                                    final ServerIdentity server) {
        return findServerMetrics(metrics, server.serverId())
                .map(metric -> SessionChoice.<String, Integer>success(metric.currentPlayerCount()))
                .orElse(SessionChoice.failure("Metrics not found for serverId: " + server.serverId()));
    }

    private static SessionChoice<String, Double> serverLoad(final List<ServerMetric> metrics,
                                                            final ServerIdentity server,
                                                            final Function<ServerMetric, Double> loadCalculator) {
        return findServerMetrics(metrics, server.serverId())
                .map(metric -> SessionChoice.<String, Double>success(loadCalculator.apply(metric)))
                .orElse(SessionChoice.failure("Metrics not found for serverId: " + server.serverId()));
    }

    private static Optional<ServerMetric> findServerMetrics(final List<ServerMetric> metrics,
                                                            final UUID serverId) {
        return metrics.stream()
                .filter(metric -> metric.serverId().equals(serverId))
                .findFirst();
    }

    private static SessionChoice<String, ServerIdentity> findServerWithLeastConnections(
            final List<ServerIdentity> servers,
            final List<ServerMetric> metrics,
            final String instance) {

        final List<ServerIdentity> filteredServers = filterServersByInstance(servers, instance);

        if (filteredServers.isEmpty()) {
            return SessionChoice.failure("No servers found for instance: " + instance);
        }

        return filteredServers.stream()
                .min(Comparator.comparingInt(server ->
                        serverPlayerCount(metrics, server)
                                .fold(count -> count, error -> Integer.MAX_VALUE)))
                .map(SessionChoice::<String, ServerIdentity>success)
                .orElse(SessionChoice.failure("Unexpected error during server selection"));
    }

    private static SessionChoice<String, ServerIdentity> findServerWithLowestLoad(
            final List<ServerIdentity> servers,
            final List<ServerMetric> metrics,
            final String instance,
            final Function<ServerMetric, Double> loadCalculator) {

        final List<ServerIdentity> filteredServers = filterServersByInstance(servers, instance);

        if (filteredServers.isEmpty()) {
            return SessionChoice.failure("No servers found for instance: " + instance);
        }

        return filteredServers.stream()
                .min(Comparator.comparingDouble(server ->
                        serverLoad(metrics, server, loadCalculator)
                                .fold(load -> load, error -> Double.MAX_VALUE)))
                .map(SessionChoice::<String, ServerIdentity>success)
                .orElse(SessionChoice.failure("Unexpected error during server selection"));
    }
}

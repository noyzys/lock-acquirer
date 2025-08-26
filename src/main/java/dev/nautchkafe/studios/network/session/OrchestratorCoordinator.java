package dev.nautchkafe.studios.network.session;

import dev.nautchkafe.studios.network.session.contract.ConnectionContract;
import dev.nautchkafe.studios.network.session.identity.PlayerIdentity;
import dev.nautchkafe.studios.network.session.identity.ServerIdentity;
import dev.nautchkafe.studios.network.session.metric.ServerMetric;
import dev.nautchkafe.studios.network.session.selection.ServerSelector;
import dev.nautchkafe.studios.network.session.validation.ConnectionValidator;
import dev.nautchkafe.studios.network.session.validation.PlayerValidator;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

final class OrchestratorCoordinator implements ConnectionOrchestrator {

    private final ConnectionValidator connectionValidator;
    private final PlayerValidator playerValidator;
    private final ServerSelector serverSelector;

    private final Supplier<List<ServerIdentity>> serverInventories;
    private final Supplier<List<ServerMetric>> serverMetrics;
    private final Function<String, PlayerIdentity> playerIdentityResolver;

    private OrchestratorCoordinator(final ConnectionValidator connectionValidator,
                                    PlayerValidator playerValidator,
                                    final ServerSelector serverSelector,
                                    final Supplier<List<ServerIdentity>> serverInventories,
                                    final Supplier<List<ServerMetric>> serverMetrics,
                                    final Function<String, PlayerIdentity> playerIdentityResolver) {
        this.connectionValidator = connectionValidator;
        this.playerValidator = playerValidator;
        this.serverSelector = serverSelector;
        this.serverInventories = serverInventories;
        this.serverMetrics = serverMetrics;
        this.playerIdentityResolver = playerIdentityResolver;
    }

    static OrchestratorCoordinator create(
            final ConnectionValidator connectionValidator,
            final PlayerValidator playerValidator,
            final ServerSelector serverSelector,
            final Supplier<List<ServerIdentity>> serverInventories,
            final Supplier<List<ServerMetric>> serverMetrics,
            final Function<String, PlayerIdentity> playerIdentityResolver) {

        return new OrchestratorCoordinator(
                connectionValidator,
                playerValidator,
                serverSelector,
                serverInventories,
                serverMetrics,
                playerIdentityResolver);
    }

    @Override
    public SessionChoice<String, ConnectionContract> orchestratePlayerConnection(final String playerId, final String targetInstance) {
        return resolvePlayerIdentity(playerId)
                .flatMap(this::validatePlayerIdentity)
                .flatMap(playerIdentity -> selectOptimalServer(targetInstance)
                        .flatMap(serverIdentity -> createConnection(playerIdentity, serverIdentity)))
                .flatMap(connectionValidator::handleConnection);
    }

    private SessionChoice<String, ConnectionContract> createConnection(
            final PlayerIdentity playerId,
            final ServerIdentity serverId) {

        final ConnectionContract connectionContract = new ConnectionContract(
                UUID.randomUUID(),
                playerId,
                serverId,
                generateAuthToken(),
                System.currentTimeMillis());

        return SessionChoice.success(connectionContract);
    }

    private SessionChoice<String, PlayerIdentity> resolvePlayerIdentity(final String playerId) {
        return SessionChoice.success(playerIdentityResolver.apply(playerId));
    }

    private SessionChoice<String, PlayerIdentity> validatePlayerIdentity(final PlayerIdentity playerId) {
        return playerValidator.validatePlayer(playerId);
    }

    private SessionChoice<String, ServerIdentity> selectOptimalServer(final String instance) {
        final List<ServerIdentity> servers = serverInventories.get();
        final List<ServerMetric> metrics = serverMetrics.get();

        return serverSelector.selectOptimalServer(servers, metrics, instance);
    }

    private String generateAuthToken() {
        return java.util.UUID.randomUUID().toString();
    }
}

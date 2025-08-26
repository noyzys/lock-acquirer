package dev.nautchkafe.studios.network.session;

import dev.nautchkafe.studios.network.session.contract.ConnectionContract;

interface ConnectionOrchestrator {

    SessionChoice<String, ConnectionContract> orchestratePlayerConnection(final String playerId, String targetInstance);
}

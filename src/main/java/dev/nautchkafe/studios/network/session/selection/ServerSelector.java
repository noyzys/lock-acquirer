package dev.nautchkafe.studios.network.session.selection;

import dev.nautchkafe.studios.network.session.identity.ServerIdentity;
import dev.nautchkafe.studios.network.session.metric.ServerMetric;
import dev.nautchkafe.studios.network.session.SessionChoice;

import java.util.List;

@FunctionalInterface
public interface ServerSelector {

    SessionChoice<String, ServerIdentity> selectOptimalServer(
            final List<ServerIdentity> availableServers,
            final List<ServerMetric> serverMetrics,
            final String instance);
}

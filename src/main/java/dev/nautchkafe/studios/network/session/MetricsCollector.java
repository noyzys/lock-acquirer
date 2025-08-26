package dev.nautchkafe.studios.network.session;

import dev.nautchkafe.studios.network.session.metric.GameSession;

@FunctionalInterface
interface MetricsCollector {

    void collectGameMetric(final GameSession gameSession);
}

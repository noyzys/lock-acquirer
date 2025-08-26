package dev.nautchkafe.studios.network.session.validation;

import dev.nautchkafe.studios.network.session.contract.ConnectionContract;
import dev.nautchkafe.studios.network.session.SessionChoice;

@FunctionalInterface
public interface ConnectionValidator {

    SessionChoice<String, ConnectionContract> handleConnection(final ConnectionContract context);
}

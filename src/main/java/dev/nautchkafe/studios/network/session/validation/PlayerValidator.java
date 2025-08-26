package dev.nautchkafe.studios.network.session.validation;

import dev.nautchkafe.studios.network.session.identity.PlayerIdentity;
import dev.nautchkafe.studios.network.session.SessionChoice;

@FunctionalInterface
public interface PlayerValidator {

    SessionChoice<String, PlayerIdentity> validatePlayer(final PlayerIdentity playerId);
}

package dev.nautchkafe.studios.network.session.selection;

import dev.nautchkafe.studios.network.session.identity.ServerIdentity;

import java.util.function.Function;
import java.util.function.Supplier;

sealed interface SelectionResult {

    record Found(ServerIdentity serverId) implements SelectionResult {
    }

    record NotFound() implements SelectionResult {
    }

    default <TYPE> TYPE fold(final Function<ServerIdentity,  TYPE> onFound,
                             final Supplier<TYPE> onNotFound) {

        return switch (this) {
            case Found found -> onFound.apply(found.serverId());
            case NotFound notFound -> onNotFound.get();
        };
    }
}

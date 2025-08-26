package dev.nautchkafe.studios.network.session;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface SessionChoice<LEFT, RIGHT> {

    record Success<LEFT, RIGHT>(RIGHT value) implements SessionChoice<LEFT, RIGHT> {
    }
    
    record Failure<LEFT, RIGHT>(LEFT value) implements SessionChoice<LEFT, RIGHT> {
    }

    default <TYPE> SessionChoice<LEFT, TYPE> map(final Function<RIGHT, TYPE> mapper) {
        return switch (this) {
            case Success<LEFT, RIGHT> success -> success(mapper.apply(success.value()));
            case Failure<LEFT, RIGHT> failure -> failure(failure.value());
        };
    }

    default <TYPE> SessionChoice<LEFT, TYPE> flatMap(final Function<RIGHT, SessionChoice<LEFT, TYPE>> mapper) {
        return switch (this) {
            case Success<LEFT, RIGHT> success -> mapper.apply(success.value());
            case Failure<LEFT, RIGHT> failure -> failure(failure.value());
        };
    }

    default SessionChoice<LEFT, RIGHT> ifSuccess(final Consumer<RIGHT> consumer) {
        if (this instanceof Success<LEFT, RIGHT>(RIGHT value)) {
            consumer.accept(value);
        }

        return this;
    }

    default RIGHT getOrElse(final RIGHT defaultValue) {
        return switch (this) {
            case Success<LEFT, RIGHT> success -> success.value();
            case Failure<LEFT, RIGHT> failure -> defaultValue;
        };
    }

    default <TYPE> TYPE fold(final Function<RIGHT, TYPE> onSuccess, final Function<LEFT, TYPE> onFailure) {
        return switch (this) {
            case Success<LEFT, RIGHT> success -> onSuccess.apply(success.value());
            case Failure<LEFT, RIGHT> failure -> onFailure.apply(failure.value());
        };
    }

    default RIGHT getOrElse(final Supplier<RIGHT> defaultValueSupplier) {
        return switch (this) {
            case Success<LEFT, RIGHT> success -> success.value();
            case Failure<LEFT, RIGHT> failure -> defaultValueSupplier.get();
        };
    }

    default SessionChoice<LEFT, RIGHT> ifFailure(final Consumer<LEFT> consumer) {
        if (this instanceof Failure<LEFT, RIGHT>(LEFT value)) {
            consumer.accept(value);
        }

        return this;
    }

    static <LEFT, RIGHT> SessionChoice<LEFT, RIGHT> success(final RIGHT value) {
        return new Success<>(value);
    }

    static <LEFT, RIGHT> SessionChoice<LEFT, RIGHT> failure(final LEFT value) {
        return new Failure<>(value);
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default RIGHT getSuccess() {
        return ((Success<LEFT, RIGHT>) this).value();
    }

    default LEFT getFailure() {
        return ((Failure<LEFT, RIGHT>) this).value();
    }
}


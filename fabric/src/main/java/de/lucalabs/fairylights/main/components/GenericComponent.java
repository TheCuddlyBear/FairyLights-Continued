package de.lucalabs.fairylights.main.components;

import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

import java.util.Optional;

public abstract class GenericComponent<T> implements CardinalComponent {

    @Nullable
    protected T delegate;

    public GenericComponent() {
        this.delegate = null;
    }

    public Optional<T> get() {
        return Optional.ofNullable(this.delegate);
    }

    public GenericComponent<T> set(T delegate) {
        this.delegate = delegate;
        return this;
    }

    public boolean isEmpty() {
        return delegate == null;
    }
}

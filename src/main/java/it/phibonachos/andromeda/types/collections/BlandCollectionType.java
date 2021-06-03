package it.phibonachos.andromeda.types.collections;

import it.phibonachos.andromeda.types.SoloConstraint;

import java.util.Collection;

public class BlandCollectionType<T, C extends Collection<T>> extends SoloConstraint<C> {

    @Override
    public Boolean validate(C guard) {
        return guard != null;
    }

    @Override
    public String message() {
        return "cannot be null or empty.";
    }
}

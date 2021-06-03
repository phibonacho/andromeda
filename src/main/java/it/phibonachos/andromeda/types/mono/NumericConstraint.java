package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.types.SoloConstraint;

import java.util.Optional;

public class NumericConstraint<NT extends Number> extends SoloConstraint<NT> {

    @Override
    public Boolean validate(NT guard) {
        return Optional.ofNullable(guard).map(val -> true).orElseThrow(NullPointerException::new);
    }
}

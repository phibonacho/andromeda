package it.phibonachos.andromeda.types;

import java.util.Optional;

public class NumericConstraint<NT extends Number> extends SingleValueConstraint<NT> {

    @Override
    public Boolean validate(NT guard) {
        return Optional.ofNullable(guard).map(val -> true).orElseThrow(NullPointerException::new);
    }
}

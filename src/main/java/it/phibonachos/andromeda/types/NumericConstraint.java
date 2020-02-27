package it.phibonachos.andromeda.types;

import java.util.InvalidPropertiesFormatException;

public class NumericConstraint<NT extends Number> extends SingleValueConstraint<NT> {

    @Override
    public Boolean validate(NT guard) {
        return guard != null;
    }
}

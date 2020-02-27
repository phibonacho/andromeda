package it.phibonachos.andromeda.types;

import java.util.InvalidPropertiesFormatException;

public class BooleanValue extends SingleValueConstraint<Boolean> {

    @Override
    public Boolean validate(Boolean guard) {
        return !guard ? null : true;
    }
}

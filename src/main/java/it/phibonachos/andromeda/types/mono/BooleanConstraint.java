package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.types.SoloConstraint;

public class BooleanConstraint extends SoloConstraint<Boolean> {

    @Override
    public Boolean validate(java.lang.Boolean guard) {
        return !guard ? null : true;
    }
}

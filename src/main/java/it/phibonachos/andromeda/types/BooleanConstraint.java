package it.phibonachos.andromeda.types;

public class BooleanConstraint extends SoloConstraint<Boolean> {

    @Override
    public Boolean validate(java.lang.Boolean guard) {
        return !guard ? null : true;
    }
}

package it.phibonachos.andromeda.types;

public class BooleanValue extends SingleValueConstraint<Boolean> {

    @Override
    public Boolean validate(Boolean guard) {
        return !guard ? null : true;
    }
}

package it.phibonachos.andromeda.types;

public abstract class CoupleConstraint<F,S> extends MultiValueConstraint {

    @Override
    @SuppressWarnings("unchecked")
    public Boolean validateAll(Object... objects) throws Exception {
        return validate((F) objects[0], (S)objects[1]);
    }

    public abstract Boolean validate(F guard, S boundGuard);
}

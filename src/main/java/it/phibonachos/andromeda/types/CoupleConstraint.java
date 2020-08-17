package it.phibonachos.andromeda.types;

public abstract class CoupleConstraint<F,S> extends MultiValueConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean validateAll(Object... objects) throws Exception {
        return convert((F) objects[0], (S)objects[1]);
    }

    public abstract Boolean convert(F guard, S boundGuard);
}

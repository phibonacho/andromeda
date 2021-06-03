package it.phibonachos.andromeda.types;

public abstract class DuetConstraint<F,S> extends MultiConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean convertAll(Object... objects) throws Exception {
        return validate((F) objects[0], (S)objects[1]);
    }

    public abstract Boolean validate(F guard, S boundGuard) throws Exception;

    @Override
    public int arity() {
        return 2;
    }
}

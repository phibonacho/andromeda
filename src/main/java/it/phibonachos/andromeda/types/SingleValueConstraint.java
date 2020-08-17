package it.phibonachos.andromeda.types;

public abstract class SingleValueConstraint<T> extends MultiValueConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean validateAll(Object... objects) throws Exception {
        return validate((T) objects[0]);
    }

    public abstract Boolean validate(T target);

}

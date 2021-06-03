package it.phibonachos.andromeda.types;

public abstract class SoloConstraint<T> extends MultiConstraint {

    @Override
    @SuppressWarnings("unchecked")
    public Boolean convertAll(Object ...objects) throws Exception {
        return validate((T) objects[0]);
    }

    public abstract Boolean validate(T target) throws Exception;

    public int arity() {
        return 1;
    }
}

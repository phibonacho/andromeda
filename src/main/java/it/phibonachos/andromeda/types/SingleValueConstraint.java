package it.phibonachos.andromeda.types;

public abstract class SingleValueConstraint<T> extends MultiValueConstraint {

    @Override
    @SuppressWarnings("unchecked")
    public Boolean convertAll(Object ...objects) throws Exception {
        return validate((T) objects[0]);
    }

    public abstract Boolean validate(T target) throws Exception;

}

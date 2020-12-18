package it.phibonachos.andromeda.types;

public abstract class OctectConstraint<F,S,T,Frth,Ffth,Sxth,Svth,E> extends MultiValueConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean convertAll(Object... objects) throws Exception {
        return validate((F) objects[0], (S)objects[1], (T)objects[2], (Frth) objects[3], (Ffth) objects[4], (Sxth) objects[5], (Svth) objects[6], (E) objects[7]);
    }

    public abstract Boolean validate(F guard, S boundGuard, T third, Frth fourth, Ffth fifth, Sxth sixth, Svth seventh, E eighth) throws Exception;

    public int arity() {
        return 8;
    }
}

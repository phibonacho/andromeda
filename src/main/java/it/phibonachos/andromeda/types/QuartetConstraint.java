package it.phibonachos.andromeda.types;

public abstract class QuartetConstraint<G1,G2,G3,G4> extends MultiConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean convertAll(Object... objects) throws Exception {
        return validate((G1) objects[0], (G2) objects[1], (G3) objects[2], (G4) objects[3]);
    }

    public abstract Boolean validate(G1 firstGuard, G2 secondGuard, G3 thirdGuard, G4 fourthGuard) throws Exception;

    @Override
    public int arity() {
        return 2;
    }
}

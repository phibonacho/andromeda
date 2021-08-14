package it.phibonachos.andromeda.types;

public abstract class SextetConstraint<G1,G2,G3,G4,G5,G6> extends MultiConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean convertAll(Object... objects) throws Exception {
        return validate((G1) objects[0], (G2) objects[1], (G3) objects[2], (G4) objects[3], (G5) objects[4], (G6) objects[5]);
    }

    public abstract Boolean validate(G1 guard, G2 boundGuard, G3 thirdGuard, G4 fourthGuard, G5 fifthGuard, G6 sixthGuard) throws Exception;

    @Override
    public int arity() {
        return 2;
    }
}

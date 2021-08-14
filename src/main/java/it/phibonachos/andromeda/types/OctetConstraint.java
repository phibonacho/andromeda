package it.phibonachos.andromeda.types;

public abstract class OctetConstraint<G1,G2,G3,G4,G5,G6,G7,G8> extends MultiConstraint {

    @Override
    @SuppressWarnings("unchecked")
    protected Boolean convertAll(Object... objects) throws Exception {
        return validate((G1) objects[0], (G2)objects[1], (G3)objects[2], (G4) objects[3], (G5) objects[4], (G6) objects[5], (G7) objects[6], (G8) objects[7]);
    }

    public abstract Boolean validate(G1 firstGuard, G2 secondGuard, G3 thirdGuard, G4 fourthGuard, G5 fifthGuard, G6 sixthGuard, G7 seventhGuard, G8 eighthGuard) throws Exception;

    public int arity() {
        return 8;
    }
}

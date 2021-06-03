package evaluators.constraints;

import it.phibonachos.andromeda.types.OctetConstraint;

public class C8Constraint extends OctetConstraint<String,String,String,String,String,String,String,String> {
    @Override
    public Boolean validate(String guard, String boundGuard, String a, String b, String c, String d, String e, String f) {
        return true;
    }

    @Override
    public String message() {
        return "banana";
    }
}

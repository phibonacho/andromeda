package evaluators.constraints;

import it.phibonachos.andromeda.types.CoupleConstraint;
import org.apache.commons.lang3.StringUtils;

public class C2Constraint extends CoupleConstraint<String, String> {
    @Override
    public Boolean convert(String guard, String boundGuard) {
        return guard.length() + boundGuard.length() < 15;
    }

    @Override
    public String message() {
        return "incompatible values for prop1 and prop2";
    }
}

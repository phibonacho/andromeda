package evaluators.constraints;

import it.phibonachos.andromeda.types.DuetConstraint;

public class C2Constraint extends DuetConstraint<String, String> {
    @Override
    public Boolean validate(String guard, String boundGuard) {
        return guard.length() + boundGuard.length() < 15;
    }

    @Override
    public String message() {
        return "incompatible values for prop1 and prop2";
    }
}

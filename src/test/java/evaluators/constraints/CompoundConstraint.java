package evaluators.constraints;

import it.phibonachos.andromeda.types.CoupleConstraint;
import org.apache.commons.lang3.StringUtils;

public class CompoundConstraint extends CoupleConstraint<Boolean, String> {
    @Override
    public Boolean validate(Boolean guard, String boundGuard) {
        return guard && !StringUtils.isBlank(boundGuard) || !guard && StringUtils.isBlank(boundGuard);
    }

    @Override
    public String message() {
        return "incompatible values for prop1 and prop2";
    }
}

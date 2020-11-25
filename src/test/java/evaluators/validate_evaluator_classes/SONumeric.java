package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.NumericConstraint;

public class SONumeric {
    private int prop;

    @Validate(with = NumericConstraint.class, mandatory = true)
    public int getProp() {
        return prop;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }
}

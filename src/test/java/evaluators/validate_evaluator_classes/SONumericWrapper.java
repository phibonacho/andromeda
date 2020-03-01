package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.NumericConstraint;
import it.phibonachos.andromeda.types.PositiveNum;

public class SONumericWrapper {
    private Integer prop = null;

    @Validate(with = PositiveNum.class, mandatory = true)
    public Integer getProp() {
        return prop;
    }

    public void setProp(Integer prop) {
        this.prop = prop;
    }
}

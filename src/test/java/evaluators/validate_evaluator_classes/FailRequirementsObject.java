package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.NumericConstraint;
import it.phibonachos.andromeda.types.StringValue;

public class FailRequirementsObject {
    private String prop;
    private Double requiredProp;

    @Validate(with = StringValue.class, mandatory = true, requires = "requiredProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = NumericConstraint.class)
    public Double getRequiredProp() {
        return requiredProp;
    }

    public void setRequiredProp(Double requiredProp) {
        this.requiredProp = requiredProp;
    }
}

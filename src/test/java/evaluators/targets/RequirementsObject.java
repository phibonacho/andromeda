package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.NumericConstraint;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class RequirementsObject {
    private java.lang.String prop;
    private Double requiredProp;

    @Validate(with = StringConstraint.class, requires = "requiredProp")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
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

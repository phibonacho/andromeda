package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class CyclicRequirementsObject {
    private java.lang.String prop;
    private java.lang.String prop1;

    @Validate(with = StringConstraint.class, requires = "prop1")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class, requires = "prop")
    public java.lang.String getProp1() {
        return prop1;
    }

    public void setProp1(java.lang.String prop1) {
        this.prop1 = prop1;
    }
}

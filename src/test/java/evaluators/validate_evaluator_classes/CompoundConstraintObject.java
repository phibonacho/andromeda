package evaluators.validate_evaluator_classes;

import evaluators.constraints.CompoundConstraint;
import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class CompoundConstraintObject {
    private java.lang.String prop;
    private boolean prop1;
    private java.lang.String prop2;

    @Validate(with = StringConstraint.class, mandatory = true)
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = CompoundConstraint.class, boundTo = "prop2")
    public boolean isProp1() {
        return prop1;
    }

    public void setProp1(boolean prop1) {
        this.prop1 = prop1;
    }

    @Validate(with = StringConstraint.class)
    public java.lang.String getProp2() {
        return prop2;
    }

    public void setProp2(java.lang.String prop2) {
        this.prop2 = prop2;
    }
}

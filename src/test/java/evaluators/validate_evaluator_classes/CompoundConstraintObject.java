package evaluators.validate_evaluator_classes;

import evaluators.constraints.CompoundConstraint;
import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.StringValue;

public class CompoundConstraintObject {
    private String prop;
    private boolean prop1;
    private String prop2;

    @Validate(with = StringValue.class, mandatory = true)
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = CompoundConstraint.class, boundTo = "prop2")
    public boolean isProp1() {
        return prop1;
    }

    public void setProp1(boolean prop1) {
        this.prop1 = prop1;
    }

    @Validate(with = StringValue.class)
    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }
}

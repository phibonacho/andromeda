package evaluators.targets;

import evaluators.constraints.C2Constraint;
import evaluators.constraints.CompoundConstraint;
import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class ComplexObject {
    private java.lang.String prop0;
    private java.lang.String prop1;
    private java.lang.String prop2;
    private Boolean prop3;
    private java.lang.String prop4;

    @Validate
    public java.lang.String getProp0() {
        return prop0;
    }

    public void setProp0(java.lang.String prop0) {
        this.prop0 = prop0;
    }

    @Validate(with = StringConstraint.class, mandatory = true)
    public java.lang.String getProp1() {
        return prop1;
    }

    public void setProp1(java.lang.String prop1) {
        this.prop1 = prop1;
    }

    @Validate(with = StringConstraint.class, mandatory = true, requires = {"prop1"}) // validation priority 2
    public java.lang.String getProp2() {
        return prop2;
    }

    public void setProp2(java.lang.String prop2) {
        this.prop2 = prop2;
    }

    @Validate(with = CompoundConstraint.class, mandatory = true, boundTo = "prop2") // validation priority 3
    public Boolean isProp3() {
        return prop3;
    }

    public void setProp3(Boolean prop3) {
        this.prop3 = prop3;
    }

    @Validate(with = C2Constraint.class, mandatory = true, requires = "prop3", boundTo = "prop1") // validation priority 4
    public java.lang.String getProp4() {
        return prop4;
    }

    public void setProp4(java.lang.String prop4) {
        this.prop4 = prop4;
    }
}

package evaluators.validate_evaluator_classes;

import evaluators.constraints.CompoundConstraint;
import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringValue;

public class ComplexObject {
    private String prop0;
    private String prop1;
    private String prop2;
    private Boolean prop3;
    private String prop4;

    @Validate
    public String getProp0() {
        return prop0;
    }

    public void setProp0(String prop0) {
        this.prop0 = prop0;
    }

    @Validate(with = StringValue.class, mandatory = true)
    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    @Validate(with = StringValue.class, mandatory = true, requires = {"prop1", "prop0"}) // validation priority 2
    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }

    @Validate(with = CompoundConstraint.class, mandatory = true, boundTo = "prop2") // validation priority 3
    public boolean isProp3() {
        return prop3;
    }

    public void setProp3(boolean prop3) {
        this.prop3 = prop3;
    }

    @Validate(with = StringValue.class, mandatory = true, requires = "prop3", boundTo = "prop1") // validation priority 4
    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4;
    }
}

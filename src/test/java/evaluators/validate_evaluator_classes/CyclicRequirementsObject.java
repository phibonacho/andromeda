package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.StringValue;

public class CyclicRequirementsObject {
    private String prop;
    private String prop1;

    @Validate(with = StringValue.class, requires = "prop1")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, requires = "prop")
    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }
}

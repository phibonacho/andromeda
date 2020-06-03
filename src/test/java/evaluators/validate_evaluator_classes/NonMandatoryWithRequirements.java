package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.StringValue;

public class NonMandatoryWithRequirements {
    private String prop;
    private String requireProp;

    @Validate(with = StringValue.class, requires = "requireProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class)
    public String getRequireProp() {
        return requireProp;
    }

    public void setRequireProp(String requireProp) {
        this.requireProp = requireProp;
    }
}

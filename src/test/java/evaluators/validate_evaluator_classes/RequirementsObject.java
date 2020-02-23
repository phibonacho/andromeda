package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.DoubleValue;
import it.phibonachos.andromeda.types.StringValue;

public class RequirementsObject {
    private String prop;
    private Double requiredProp;

    @Validate(with = StringValue.class, require = "requiredProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = DoubleValue.class)
    public Double getRequiredProp() {
        return requiredProp;
    }

    public void setRequiredProp(Double requiredProp) {
        this.requiredProp = requiredProp;
    }
}

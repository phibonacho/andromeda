package phb.testClasses.validate_evaluator_classes;

import phb.annotation.validate.Validate;
import phb.annotation.validate.types.DoubleValue;
import phb.annotation.validate.types.StringValue;

public class RequirementsObject {
    private String prop;
    private Double requiredProp;

    @Validate(with = StringValue.class, require = "getRequiredProp")
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

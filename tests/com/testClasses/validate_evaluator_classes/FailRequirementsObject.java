package com.testClasses.validate_evaluator_classes;

import com.annotation.validate.Validate;
import com.annotation.validate.types.DoubleValue;
import com.annotation.validate.types.StringValue;

public class FailRequirementsObject {
    private String prop;
    private Double requiredProp;

    @Validate(with = StringValue.class, mandatory = true, require = "getRequiredProp")
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

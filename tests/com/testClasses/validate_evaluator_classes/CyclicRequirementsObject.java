package com.testClasses.validate_evaluator_classes;

import com.annotation.validate.Validate;
import com.annotation.validate.types.StringValue;

public class CyclicRequirementsObject {
    private String prop;
    private String prop1;

    @Validate(with = StringValue.class, require = "getProp1")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, require = "getProp")
    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }
}

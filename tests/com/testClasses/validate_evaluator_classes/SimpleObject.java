package com.testClasses.validate_evaluator_classes;

import com.annotation.validate.Validate;
import com.annotation.validate.types.StringValue;

public class SimpleObject {
    private String prop;

    @Validate(with = StringValue.class, mandatory = true)
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}

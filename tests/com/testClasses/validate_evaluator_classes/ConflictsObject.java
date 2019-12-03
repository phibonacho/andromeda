package com.testClasses.validate_evaluator_classes;

import com.annotation.validate.Validate;
import com.annotation.validate.types.DoubleValue;
import com.annotation.validate.types.StringValue;

public class ConflictsObject {
    private String prop;
    private String conflictProp;

    @Validate(with = StringValue.class, conflicts = "getConflictProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, conflicts = "getProp")
    public String getConflictProp() {
        return conflictProp;
    }

    public void setConflictProp(String requiredProp) {
        this.conflictProp = requiredProp;
    }
}

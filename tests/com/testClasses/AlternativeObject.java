package com.testClasses;

import com.annotation.validate.Validate;
import com.annotation.validate.types.StringValue;

public class AlternativeObject {
    private String prop;
    private String alternativeProp;

    @Validate(with = StringValue.class, mandatory = true, alternatives = "getAlternativeProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class)
    public String getAlternativeProp() {
        return alternativeProp;
    }

    public void setAlternativeProp(String alternativeProp) {
        this.alternativeProp = alternativeProp;
    }
}

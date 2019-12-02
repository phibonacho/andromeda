package com.testClasses;

import com.annotation.validate.Validate;
import com.annotation.validate.types.StringValue;

public class CascadeAlternativeObject {
    private String prop;
    private String aProp;
    private String aProp1;


    @Validate(with = StringValue.class, mandatory = true, alternatives = "getAProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, alternatives = "getAProp1")
    public String getAProp() {
        return aProp;
    }

    public void setAlternativeProp(String alternativeProp) {
        this.aProp = alternativeProp;
    }

    public String getAProp1() {
        return aProp1;
    }

    public void setAProp1(String aProp1) {
        this.aProp1 = aProp1;
    }
}

package com.annotation.types;

public abstract class AbstractStringType extends AbstractValidateType<String> {

    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof String)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match String");
        return true;
    }

}

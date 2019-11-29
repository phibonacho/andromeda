package com.annotation.validate.types;

public class FloatValue extends AbstractNumericType {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Float)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Float");
        return true;
    }

}

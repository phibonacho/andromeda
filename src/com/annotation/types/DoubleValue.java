package com.annotation.types;

public class DoubleValue extends AbstractNumericType {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Double)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Double");
        return true;
    }
}

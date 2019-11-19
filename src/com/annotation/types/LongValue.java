package com.annotation.types;

public class LongValue extends AbstractNumericType {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Long)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Long");
        return true;
    }
}

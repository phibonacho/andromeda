package com.annotation.comparison.types;

import com.annotation.comparison.exception.InvalidFieldException;

import java.util.InvalidPropertiesFormatException;

public abstract class AbstractCompareType<I extends Number> implements CompareTypeInterface<Boolean, I> {

    protected AbstractCompareType() {}

    public <T extends I> Boolean compare(I input, T threshold) throws IllegalArgumentException, InvalidPropertiesFormatException {
        return isInstance(input, threshold) && check(input, threshold);
    }

    private <T extends I> Boolean isInstance(I value, T obj) {
        if(value.getClass() != obj.getClass()) throw new InvalidFieldException(obj.getClass() + " cannot be casted to " + value.getClass());
        return true;
    }

    public abstract Boolean check(I input, I threshold) throws InvalidPropertiesFormatException;
}

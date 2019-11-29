package com.annotation.comparison.types;

import com.annotation.comparison.exception.InvalidFieldException;

import java.util.InvalidPropertiesFormatException;

public class Greater<I extends Number> extends AbstractCompareType<I> {

    @Override
    public Boolean check(I input, I threshold) throws InvalidPropertiesFormatException {
        return input.doubleValue() >= threshold.doubleValue();
    }

    @Override
    public Boolean compare(I guard) throws InvalidPropertiesFormatException {
        return null;
    }
}
package com.annotation.comparison.types;

import java.lang.reflect.InvocationTargetException;
import java.util.InvalidPropertiesFormatException;

public interface CompareTypeInterface<ControlType, GuardType> {
    static <T extends CompareTypeInterface> T create(Class<T> vti) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return vti.getDeclaredConstructor().newInstance();
    }

    ControlType compare(GuardType guard) throws InvalidPropertiesFormatException;
}

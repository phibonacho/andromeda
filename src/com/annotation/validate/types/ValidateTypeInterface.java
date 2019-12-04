package com.annotation.validate.types;

import java.lang.reflect.InvocationTargetException;
import java.util.InvalidPropertiesFormatException;

public interface ValidateTypeInterface<ControlType, GuardType> {
    static <T extends ValidateTypeInterface> T create(Class<T> vti) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return vti.getDeclaredConstructor().newInstance();
    }

    ControlType validate(GuardType guard) throws Exception;
}

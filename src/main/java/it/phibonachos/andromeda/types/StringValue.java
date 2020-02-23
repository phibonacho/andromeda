package it.phibonachos.andromeda.types;

import org.apache.commons.lang3.StringUtils;

import java.util.InvalidPropertiesFormatException;

public class StringValue extends AbstractStringType {
    @Override
    public Boolean check(String guard) throws InvalidPropertiesFormatException {
        if(StringUtils.isBlank(guard)) throw new InvalidPropertiesFormatException("blank string passed");
        return true;
    }
}
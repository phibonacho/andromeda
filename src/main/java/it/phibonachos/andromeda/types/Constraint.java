package it.phibonachos.andromeda.types;

import it.phibonachos.ponos.converters.Converter;

import java.util.Set;

public interface Constraint<ControlType> extends Converter<ControlType> {
    void setContext(Set<String> context);
    void setIgnoreContext(Set<String> ignoreContext);
}

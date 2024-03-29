package it.phibonachos.andromeda.exception;

import it.phibonachos.ponos.converters.ConverterException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvalidNestedFieldException extends ConverterException {

    public InvalidNestedFieldException(String message) {
        super(message);
    }

    public InvalidNestedFieldException(Method method) {
        super(Stream.of(method.getName()).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining()) + " cannot be null");
    }

    public InvalidNestedFieldException(Method method, List<String> alternatives) {
        this("Illegal state, set at least one of these values: " + Stream.concat(alternatives.stream(), Stream.of(method.getName())).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).reduce((s1, s2) ->s1 +", "+s2).orElse(""));
    }
}

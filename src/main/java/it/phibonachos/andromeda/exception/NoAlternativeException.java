package it.phibonachos.andromeda.exception;

import it.phibonachos.ponos.converters.ConverterException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoAlternativeException extends ConverterException {

    private static final long serialVersionUID = -2042599547601466529L;

    public NoAlternativeException(String message) {
        super(message);
    }

    public NoAlternativeException(String methodName, List<String> alternatives) {
        this( Stream.of(methodName).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining())
                + (alternatives.size() > 0
                ? " or " + String.join(", ", alternatives) : "")
                + " as fallback" + (alternatives.size() > 1 ? "s"  : "")
        );
    }
}

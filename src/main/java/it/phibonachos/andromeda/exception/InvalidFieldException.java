package it.phibonachos.andromeda.exception;

import it.phibonachos.ponos.converters.ConverterException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvalidFieldException extends ConverterException {
    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException(String methodName, List<String> alternatives) {
        this(alternatives.size() == 0
                ? Stream.of(methodName)
                    .map(name -> name.replaceAll("^(get|is|has)", ""))
                    .map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1)))
                    .collect(Collectors.joining())
                    + " cannot be null"
                : "Illegal state, set at least one of these values: "
                    + Stream.concat(alternatives.stream(), Stream.of(methodName))
                    .collect(Collectors.joining(", ")));
    }
}

package it.phibonachos.andromeda.exception;

import it.phibonachos.ponos.converters.ConverterException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConflictFieldException extends ConverterException {
    public ConflictFieldException(String message) {
        super(message);
    }

    public ConflictFieldException(String methodName, List<String> conflicts) {
        this(Stream.of(methodName)
                .map(name -> name.replaceAll("^(get|is|has)", ""))
                .map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1)))
                .collect(Collectors.joining())
                + (conflicts.size() > 0
                    ? " clashes with: "
                        + String.join(", ", conflicts)
                    : " do not clash with anything (then why this message?!)")
                + ".");
    }
}

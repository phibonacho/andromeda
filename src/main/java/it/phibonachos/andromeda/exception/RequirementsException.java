package it.phibonachos.andromeda.exception;

import it.phibonachos.ponos.converters.ConverterException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequirementsException extends ConverterException {
    public RequirementsException(String message) {
        super(message);
    }

    public RequirementsException(String methodName, List<String> requirements) {
        this(Stream.of(methodName)
                .map(name -> name.replaceAll("^(get|is|has)", ""))
                .map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1)))
                .collect(Collectors.joining())
            + (requirements.size() > 0
            ? " requires fields: " + String.join(", ", requirements)
            : " do not have any requirements (then why this message?!)")
            + ".");
    }

}

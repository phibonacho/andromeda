package it.phibonachos.andromeda.exception;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoAlternativeException extends RuntimeException {

    private static final long serialVersionUID = -2042599547601466529L;

    public NoAlternativeException(String message) {
        super(message);
    }

    public NoAlternativeException(Method method, List<String> alternatives) {
        this( Stream.of(method.getName()).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining())
                + " or "
                + alternatives.stream()
                    .map(name -> name.replaceAll("^(get|is|has)", ""))
                    .map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1)))
                    .reduce((s1, s2) ->s1 +", "+s2).orElse("")
                + " as fallback" + (alternatives.size() > 1 ? "s"  : ""));
    }

}

package phb.annotation.validate.exception;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException(Method method) {
        super(method.getName().replaceAll("^(get|is|has)", "").toLowerCase() + " cannot be null");
    }

    public InvalidFieldException(Method method, List<String> alternatives) {
        this("Illegal state, set at least one of these values: " + Stream.concat(alternatives.stream(), Stream.of(method.getName())).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).reduce((s1, s2) ->s1 +", "+s2).orElse(""));
    }
}

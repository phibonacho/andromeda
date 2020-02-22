package phb.annotation.validate.exception;

import java.lang.reflect.Method;
import java.util.List;

public class AnnotationException extends RuntimeException {
    public AnnotationException(String message) {
        super(message);
    }
}

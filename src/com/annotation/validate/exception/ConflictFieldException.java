package com.annotation.validate.exception;

import java.lang.reflect.Method;
import java.util.List;

public class ConflictFieldException extends RuntimeException {
    public ConflictFieldException(String message) {
        super(message);
    }

    public ConflictFieldException(Method method, List<String> conflicts) {
        this(method.getName().replaceAll("^(get|is|has)", "").toLowerCase() + (conflicts.size() > 0
                ? " clashes with: " + conflicts.stream().map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).reduce((s1, s2) ->s1 +", "+s2).orElse("")
                : " do not clash with anything (then why this message?!)")
                + ".");
    }
}

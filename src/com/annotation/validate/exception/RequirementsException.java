package com.annotation.validate.exception;

import java.lang.reflect.Method;
import java.util.List;

public class RequirementsException extends RuntimeException {
    public RequirementsException(String message) {
        super(message);
    }

    public RequirementsException(Method method, List<String> requirements) {
        this(method.getName().replaceAll("^(get|is|has)", "").toLowerCase() + (requirements.size() > 0
                ? " requires fields: " + requirements.stream().map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).reduce((s1, s2) ->s1 +", "+s2).orElse("")
                : " do not have any requirements (then why this message?!)")
                + ".");
    }

}

package it.phibonachos.andromeda;

import it.phibonachos.andromeda.types.MultiValueConstraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validate {

    enum Ignore {REQUIREMENTS, MANDATORY, ALTERNATIVES, CONFLICTS}

    /**
     * @return a validator object
     */
    Class<? extends MultiValueConstraint> with();

    /**
     * @return true if validate field is mandatory
     */
    boolean mandatory() default false;

    /**
     * @return a set of viable alternatives to current method
     */
    String[] alternatives() default {};

    String[] conflicts() default {};

    String[] requires() default {};

    String[] boundTo() default {};

    String[] context() default {};

}

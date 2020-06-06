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

    /**
     * @return a set of conflictual fields to current method
     */
    String[] conflicts() default {};

    /**
     * @return a set of requirements for current method
     */
    String[] requires() default {};

    /**
     * @return a set of properties bounded to the validation of current method
     */
    String[] boundTo() default {};

    /**
     * @return a set of contexts in which current property is included
     */
    String[] context() default {};

}

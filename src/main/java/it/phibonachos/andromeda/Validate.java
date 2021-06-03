package it.phibonachos.andromeda;

import it.phibonachos.andromeda.types.DuetConstraint;
import it.phibonachos.andromeda.types.MultiConstraint;
import it.phibonachos.andromeda.types.mono.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validate {

    /**
     * <p>Ignore enum states which clauses can be ignore by a validator.</p>
     */
    enum Ignore {REQUIREMENTS, MANDATORY, ALTERNATIVES, CONFLICTS}

    /**
     * <p>With clause states which class must be use in order to validate annotated property.
     * All validation classes extends abstract class {@link MultiConstraint}, and expose a validate(T target), which wraps validation algorithm.</p>
     *
     * @return a validator object
     */
    Class<? extends MultiConstraint> with() default NotNull.class;

    /**
     * <p>Mandatory clause states if annotated property can be whether nullable or not.
     * Mandatory clause's behaviour can be altered using contexts or {@link Ignore}.</p>
     *
     * @return true if validate field is mandatory
     */
    boolean mandatory() default false;

    /**
     * <p>Alternatives clause defines fallback properties for the annotated one, it also define a topological hierarchy in property graph.
     * Alternatives plays a crucial role in mandatory properties as they will be validated (inheriting the mandatory clause), if the mandatory property will result not set.
     * If at least one of the properties listed will result valid, the target property will result valid as well.</p>
     *
     * @return a set of viable alternatives to current method
     */
    String[] alternatives() default {};

    /**
     * <p>Conflicts clause defines a set of properties that shouldn't be set if annotated property is set.</p>
     * @return a set of conflictual fields to current method
     */
    String[] conflicts() default {};

    /**
     * <p>Requires clause defines target property's dependencies.
     * Validation will result valid only if all requirements are met.</p>
     *
     * @return a set of requirements for current method
     */
    String[] requires() default {};

    /**
     * <p>BoundTo clause defined which properties are closely connected to the annotated property.
     * During evaluation boundTo properties values will be used in order to emit a verdict, properties annotated with a boundTo clause, must use a validation class which extends {@link MultiConstraint} or at least {@link DuetConstraint}.</p>
     *
     * @return a set of properties bounded to the validation of current method
     */
    String[] boundTo() default {};

    /**
     * <p>Contexts clause associate annotated property to a specific contexts, contexts are used by macro functions {@link ValidateEvaluator#onlyContexts(String...)} and {@link ValidateEvaluator#ignoreContexts(String...)}.</p>
     *
     * @return a set of contexts in which current property is included
     */
    String[] context() default {};

}

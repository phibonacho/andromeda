package com.evaluators;

import com.annotation.validate.Validate;
import com.annotation.validate.exception.AnnotationException;
import com.annotation.validate.exception.ConflictFieldException;
import com.annotation.validate.exception.InvalidFieldException;
import com.annotation.validate.exception.RequirementsException;
import com.annotation.validate.types.ValidateTypeInterface;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class ValidateEvaluator<Target> extends AbstractEvaluator<Target, Boolean, Validate> {
    private Set<Validate.Ignore> ignoreList;

    public ValidateEvaluator(Target t) {
        super(t);
        this.annotationClass = Validate.class;
        ignoreList = new HashSet<>();
    }

    public ValidateEvaluator ignoring(Validate.Ignore ...ignorable){
        this.ignoreList = Set.of(ignorable);
        return this;
    }

    @Override
    public Boolean validate(){
        return super.validate();
    }

    /**
     * @param s the stream containing field validate evaluation
     * @return true if all fields in object are valid
     * @throws RequirementsException if a field requires the validation of another field
     * @throws InvalidFieldException if a field do not match a specific format
     * @throws ConflictFieldException if a field conflicts with at least another one
     * @throws AnnotationException if a field inherit or use a non-coherent annotation
     */
    @Override
    public Boolean evaluate(Stream<Boolean> s) throws RequirementsException, InvalidFieldException, ConflictFieldException, AnnotationException {
        return s.filter(bool -> !bool).findFirst().orElse(true); // lazy find first false validation
    }

    @Override
    protected Function<Method, Boolean> validateAlgorithm() {
        return invokeAndHandle(m -> !validateMethod(m) || (checkRequirements(m)  && checkConflicts(m)), this::validateAlternatives);
    }

    @Override
    protected Function<Method, Boolean> sortPredicate() {
        return m -> m.getAnnotation(annotationClass).mandatory();
    }


    /* ----------------- PRIVATE METHODS ----------------- */

    /**
     * Validate method with a generic validate interface
     * @param v a Validate annotation use for validate paramGetter result
     * @param paramGetter a method to validate
     * @return true if paramGetter return a valid value
     * @throws Exception if not valid
     */
    @SuppressWarnings("unchecked") // giusto perché mi fa schifo vederlo tutto giallo...
    private Boolean validateMethod(Validate v, Method paramGetter) throws Exception {
        return ValidateTypeInterface.create(v.with()).validate(paramGetter.invoke(this.t));
    }

    /**
     * Short hand for {@link #validateMethod(Validate, Method)} uses method own validate annotation
     */
    private Boolean validateMethod(Method m) throws Exception {
        return validateMethod(m.getAnnotation(annotationClass), m);
    }

    /**
     * @param method a mandatory method which returned a null value
     * @return true if at least one of the alternatives is valid or mandatory is ignorable
     * @throws InvalidFieldException if no non-null alternatives are found or alternatives are ignored
     */
    private boolean validateAlternatives(Method method) throws InvalidFieldException{
        Validate ann = method.getAnnotation(annotationClass);

        if(isIgnorable(Validate.Ignore.ALTERNATIVES))
            throw new InvalidFieldException(method, List.of(ann.alternatives()));

        if(isIgnorable(Validate.Ignore.MANDATORY) || !ann.mandatory())
            return true;

        return Arrays.stream(ann.alternatives())
                .map(fetchMethod(mName ->  this.t.getClass().getDeclaredMethod(mName)))
                .map(invokeAndHandle(
                        m -> !checkChildRequirements(m) || (validateChildMethod(ann, m) && checkChildConflicts(m)),
                        () -> false))
                // lazy find first valid
                .filter(valid -> valid)
                .findFirst().orElseThrow(() -> new InvalidFieldException(method, List.of(ann.alternatives())));
    }

    /**
     * retrieve conflictual methods with one passed as argument
     * @param m method with possible conflicts
     * @return true if no conflicts found
     * @throws ConflictFieldException if conflicts are found
     */
    private Boolean checkConflicts(Method m) throws ConflictFieldException {
        Validate ann = m.getAnnotation(annotationClass);
        if(!isIgnorable(Validate.Ignore.CONFLICTS)
                && List.of(ann.conflicts())
                .stream()
                .map(fetchMethod(mName -> this.t.getClass().getDeclaredMethod(mName)))
                .map(invokeAndHandle(method -> validateChildMethod(ann, method), () -> false))
                // cerca il primo metodo con buona validazione ed esce per lanciare eccezione
                .filter(valid -> valid)
                .findFirst().orElse(false)) throw new ConflictFieldException(m, List.of(ann.conflicts()));
        return true;
    }

    /**
     * Requirements must be met by parent mandatory annotations and child annotation of a mandatory parent
     * @param m Method with requirements to satisfy
     * @return true if all requirements are met
     * @throws RequirementsException if some requirements are not met
     */
    private Boolean checkRequirements(Method m) throws RequirementsException {
        Validate validate = m.getAnnotation(annotationClass);
        if(isIgnorable(Validate.Ignore.REQUIREMENTS)
                || List.of(validate.require())
                .stream()
                // create a stream of require methods (not null)
                .map(fetchMethod(mName -> this.t.getClass().getDeclaredMethod(mName)))
                // try to validate methods with their own annotation
                .map(invokeAndHandle(method -> !validateChildMethod(validate, method) || checkChildRequirements(method) && checkChildConflicts(method), () -> false ))
                .filter(valid -> !valid)
                .findFirst().orElse(true)) return true;
        throw new RequirementsException(m, List.of(validate.require()));
    }

    /**
     * Same function as {@link #validateMethod(Validate, Method)} but use method's father annotation if not annotated
     */
    private Boolean validateChildMethod(Validate v, Method method) throws Exception {
        return validateMethod(Optional.ofNullable(method.getAnnotation(annotationClass)).orElse(v), method);
    }

    /**
     * Shorthand for {@link #checkConflicts(Method)}, in most case, required or conflictual object are not annotated
     * this means that they have no specific conflicts or have same as parent
     */
    private Boolean checkChildConflicts(Method method) throws RequirementsException {
        return method.getAnnotation(annotationClass)==null || checkConflicts(method);
    }

    /**
     * Shorthand for {@link #checkRequirements(Method)}, in most case, required or conflictual object are not annotated
     * this means that they have no specific requirements or have same as parent
     */
    private Boolean checkChildRequirements(Method method) throws RequirementsException {
        return method.getAnnotation(annotationClass) == null || checkRequirements(method);
    }

    private boolean isIgnorable(Validate.Ignore ignorable){
        return ignoreList.contains(ignorable);
    }
}

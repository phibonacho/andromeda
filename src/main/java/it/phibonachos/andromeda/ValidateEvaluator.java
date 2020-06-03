package it.phibonachos.andromeda;

import it.phibonachos.andromeda.exception.*;
import it.phibonachos.andromeda.types.Constraint;
import it.phibonachos.andromeda.types.SingleValueConstraint;
import it.phibonachos.evaluators.AbstractEvaluator;
import it.phibonachos.utils.FunctionalWrapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidateEvaluator<Target> extends AbstractEvaluator<Target, Boolean, Validate> {
    private Set<Validate.Ignore> ignoreList;
    private Set<String> contexts, ignoreContexts;
    private Map<String, ValidationState> av;

    private enum ValidationState {VALID, NOT_SET, ON_EVALUATION, NOT_YET_EVALUATED}

    public ValidateEvaluator(Target t) {
        super(t);
        this.annotationClass = Validate.class;
        ignoreList = new HashSet<>();
        contexts = new HashSet<>();
        ignoreContexts = new HashSet<>();
        av = new HashMap<>();
    }

    public ValidateEvaluator<Target> ignoreClauses(Validate.Ignore ...ignorable){
        this.ignoreList = Set.of(ignorable);
        return this;
    }

    public ValidateEvaluator<Target> ignoreContexts(String ...ignorable){
        this.ignoreContexts = Set.of(ignorable);
        return this;
    }

    public ValidateEvaluator<Target> onlyContexts(String ...contexts){
        this.contexts = Set.of(contexts);
        return this;
    }


    @Override
    public Boolean validate(){
        av.forEach((key, value) -> av.put(key, ValidationState.NOT_YET_EVALUATED)); // reset keys in case of reuse, prevent fail on cascade requirements
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
        return invokeOnNull(m -> !validateMethod(m) || (checkRequirements(m)  && checkConflicts(m)), this::validateAlternatives);
    }

    @Override
    protected Function<Method, Boolean> sortPredicate() {
        return m -> !getMainAnnotation(m).mandatory() && getMainAnnotation(m).boundTo().length == 0;
    }

    /**
     * @param m the method to be filtered
     * @return true if the method is associated in a context registered in the validator.
     */
    @Override
    protected Boolean customFilter(Method m) {
        if(contexts.isEmpty())
            return true;

        return Arrays.stream(getMainAnnotation(m).context()).anyMatch(ctx -> contexts.contains(ctx));
    }

    /**
     * Validate method with a generic validate interface
     * @param v a Validate annotation use for validate method result
     * @param methods, method to validate
     * @return true if method return a valid value
     * @throws Exception if not valid
     */
    protected Boolean evaluateMethod(Validate v, Method ...methods) throws Exception {
        Constraint<Boolean> validator = Constraint.create(v.with());

        if(validator instanceof SingleValueConstraint)
            return validator.evaluate(this.t, methods[0]);
        return validator.evaluate(this.t, methods);
    }

    /**
     * @param throwingFunction a function capable of throwing exceptions
     * @param fallback a function to call in case of failure
     * @param <R> a return type
     * @return the result of the evaluation of throwing function or fallback
     * @throws InvalidFieldException if evaluation non-null but invalid
     */
    protected  <R>Function<Method, R> invokeOnNull(FunctionalWrapper<Method, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.get();
            } catch (InvalidCollectionFieldException e) {
                throw new InvalidFieldException("Collection " + displayName(i.getName()) + "[] : " + e.getMessage());
            } catch (InvalidPropertiesFormatException | InvalidFieldException | InvalidNestedFieldException e) {
                throw new InvalidFieldException(displayName(i.getName()) + e.getMessage());
            } catch (RequirementsException | NoAlternativeException e) {
                throw new RequirementsException(displayName(i.getName()) + " requires " + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new AnnotationException(e.getMessage() + ". Have you annotated your field correctly?");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }

    protected <R>Function<Method, R> invokeOnNull(FunctionalWrapper<Method, R, Exception> throwingFunction, Function<Method, R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.apply(i);
            } catch (InvalidCollectionFieldException e) {
                throw new InvalidFieldException("Collection " + displayName(i.getName()) + "[] : " + e.getMessage());
            } catch (InvalidPropertiesFormatException | InvalidFieldException | InvalidNestedFieldException e) {
                throw new InvalidFieldException(displayName(i.getName()) + e.getMessage());
            } catch (ConflictFieldException e) {
                throw new ConflictFieldException(e.getMessage());
            } catch (CyclicRequirementException e) {
                throw new CyclicRequirementException(e.getMessage());
            } catch (RequirementsException | NoAlternativeException e) {
                throw new RequirementsException(displayName(i.getName()) + " requires " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
    }


    /* ----------------- PRIVATE METHODS ----------------- */

    private ValidationState check(Method method) throws InvalidFieldException{
        String mName = method.getName();
        return Optional.ofNullable(av.get(mName)).orElse(ValidationState.NOT_YET_EVALUATED);

    }

    /**
     * Short hand for {@link #evaluateMethod(Validate, Method...)} uses method own validate annotation
     */
    private Boolean validateMethod(Method method) throws Exception {
        List<Method> boundMethod = Arrays.stream(getMainAnnotation(method).boundTo())
                .map(FunctionalWrapper.tryCatch(name -> new PropertyDescriptor(name, this.t.getClass()).getReadMethod()))
                .collect(Collectors.toList());

        boundMethod.add(0, method);
        if(check(method) == ValidationState.VALID)
            return true;
        if(check(method) == ValidationState.NOT_YET_EVALUATED)
            av.put(method.getName(), ValidationState.ON_EVALUATION);
        return !evaluateMethod(getMainAnnotation(method), boundMethod.toArray(new Method[0])) || (checkRequirements(method)  && checkConflicts(method));
    }

    /**
     * @param method a mandatory method which returned a null value
     * @return true if at least one of the alternatives is valid or mandatory is ignorable
     * @throws InvalidFieldException if no non-null alternatives are found or alternatives are ignored
     */
    private boolean validateAlternatives(Method method) throws InvalidFieldException{
        av.put(method.getName(), ValidationState.NOT_SET);
        Validate ann = getMainAnnotation(method);

        if(isIgnorable(Validate.Ignore.MANDATORY) || !ann.mandatory() || Arrays.stream(ann.context()).anyMatch(ctx -> ignoreContexts.contains(ctx)))
            return true;

        if(isIgnorable(Validate.Ignore.ALTERNATIVES) || ann.alternatives().length == 0)
            throw new InvalidFieldException(method);

        return validateAlternatives(method, ann);
    }

    /**
     * retrieve conflictual methods with one passed as argument
     * @param m method with possible conflicts
     * @return true if no conflicts found
     * @throws ConflictFieldException if conflicts are found
     */
    private Boolean checkConflicts(Method m) throws ConflictFieldException {
        Validate ann = getMainAnnotation(m);
        if(!isIgnorable(Validate.Ignore.CONFLICTS)
                && List.of(ann.conflicts())
                .stream()
                .map(FunctionalWrapper.tryCatch(mName ->  new PropertyDescriptor(mName, this.t.getClass()).getReadMethod()))
                .map(invokeOnNull(method -> validateChildMethod(ann, method), () -> false))
                .filter(valid -> valid)
                .findFirst().orElse(false)) throw new ConflictFieldException(m, List.of(ann.conflicts()));
        av.put(m.getName(), ValidationState.VALID);
        return true;
    }

    /**
     * Requirements must be met by parent mandatory annotations and child annotation of a mandatory parent
     * @param method with requirements to satisfy
     * @return true if all requirements are met
     * @throws RequirementsException if some requirements are not met
     */
    private Boolean checkRequirements(Method method) throws RequirementsException, InvalidFieldException {
        if(isIgnorable(Validate.Ignore.REQUIREMENTS)) // if method already validated return true
            return true;

        Validate ann = getMainAnnotation(method);
        av.put(method.getName(), ValidationState.ON_EVALUATION);

        // check requirements for field
        if(List.of(ann.requires())
                .stream()
                // create a stream of require methods (not null)
                .map(FunctionalWrapper.tryCatch(mName ->  new PropertyDescriptor(mName, this.t.getClass()).getReadMethod()))
                // try to validate methods with their own annotation
                .map(invokeOnNull(m -> !validateChildMethod(ann, m) || checkChildRequirements(m) && checkChildConflicts(m), m -> validateChildAlternatives(m, new RequirementsException(method, List.of(ann.requires())))))
                .filter(valid -> !valid)
                .findFirst().orElse(true)) return true;
        throw new RequirementsException(method, List.of(ann.requires()));
    }

    /**
     * {@link #evaluateMethod(Validate, Method...)} wrapper but use a fallback annotation if not annotated
     */
    private Boolean validateChildMethod(Validate v, Method method) throws Exception {
        return isAssessable(method.getName()) || evaluateMethod(Optional.ofNullable(getMainAnnotation(method)).orElse(v), method);
    }

    private <E extends RuntimeException>boolean validateChildAlternatives(Method method, E exception) throws InvalidFieldException{
        if(check(method) == ValidationState.VALID) // if method already validated return true
            return true;

        Validate ann = getMainAnnotation(method);

        if(isIgnorable(Validate.Ignore.ALTERNATIVES))
            throw new InvalidFieldException(method);

        return validateAlternatives(method, ann);
    }

    /**
     * Shorthand for {@link #checkConflicts(Method)}, in most case, required or conflictual object are not annotated
     * this means that they have no specific conflicts or have same as parent
     */
    private Boolean checkChildConflicts(Method method) throws RequirementsException {
        return getMainAnnotation(method) == null || checkConflicts(method);
    }

    /**
     * Shorthand for {@link #checkRequirements(Method)}, in most case, required or conflictual object are not annotated
     * this means that they have no specific requirements or have same as parent
     */
    private Boolean checkChildRequirements(Method method) throws RequirementsException {
        if(av.containsKey(method.getName()) && av.get(method.getName()).equals(ValidationState.ON_EVALUATION))
            throw new CyclicRequirementException("Detected cyclic dependency with " + method.getName());
        return getMainAnnotation(method) == null || checkRequirements(method);
    }

    private boolean isIgnorable(Validate.Ignore ignorable){
        return ignoreList.contains(ignorable);
    }

    /**
     * @return true if the property hasn't been already evaluated or it is valid
     */
    private boolean isAssessable(String propertyName) {
        return av.containsKey(propertyName) && ! (av.get(propertyName).equals(ValidationState.NOT_SET) || av.get(propertyName).equals(ValidationState.ON_EVALUATION));
    }

    private String displayName(String method){
        return Stream.of(method).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining());
    }

    private boolean validateAlternatives(Method method, Validate ann) {
        return Arrays.stream(ann.alternatives())
                .map(FunctionalWrapper.tryCatch(mName ->  new PropertyDescriptor(mName, this.t.getClass()).getReadMethod()))
                .map(invokeOnNull(
                        m -> !validateChildMethod(ann, m) || (checkChildRequirements(m) && checkChildConflicts(m)),
                        m -> false))
                // lazy find first valid
                .filter(valid -> valid)
                .findFirst().orElseThrow(() -> new NoAlternativeException(method, List.of(ann.alternatives())));
    }

}

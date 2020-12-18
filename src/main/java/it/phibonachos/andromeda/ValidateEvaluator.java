package it.phibonachos.andromeda;

import it.phibonachos.andromeda.exception.*;
import it.phibonachos.andromeda.types.Constraint;
import it.phibonachos.ponos.AbstractEvaluator;
import it.phibonachos.ponos.converters.Converter;
import it.phibonachos.utils.FunctionalUtils;
import it.phibonachos.utils.FunctionalWrapper;
import org.apache.commons.lang3.ArrayUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidateEvaluator<Target> extends AbstractEvaluator<Target, Boolean, Validate, InvalidFieldException> implements Validator {
    final private Map<String, ValidationState> av;
    final private Map<String, Object> cache;
    private Set<Validate.Ignore> ignoreList;
    private Set<String> contexts, ignoreContexts;

    private enum ValidationState {VALID, NOT_SET, ON_EVALUATION, NOT_YET_EVALUATED}

    public ValidateEvaluator(Target t) {
        super(t);
        this.annotationClass = Validate.class;
        av = new HashMap<>();
        cache = new HashMap<>();
        ignoreList = new HashSet<>();
        contexts = new HashSet<>();
        ignoreContexts = new HashSet<>();
    }

    /**
     * @param ignorable Clauses to ignore during validation
     * @return a loosen validator
     */
    public ValidateEvaluator<Target> ignoreClauses(Validate.Ignore... ignorable) {
        this.ignoreList = Set.of(ignorable);
        return this;
    }

    /**
     * @param ignorable Contexts to ignore during validation
     * @return a validator who will ignore properties associated to contexts passed as arguments
     */
    public ValidateEvaluator<Target> ignoreContexts(String... ignorable) {
        this.ignoreContexts = Set.of(ignorable);
        return this;
    }

    /**
     * @param contexts Contexts to which validation must be restricted
     * @return a specialized validator for the contexts passed as arguments
     */
    public ValidateEvaluator<Target> onlyContexts(String... contexts) {
        this.contexts = Set.of(contexts);
        return this;
    }

    public Boolean validate() {
        av.forEach((key, value) -> av.put(key, ValidationState.NOT_YET_EVALUATED)); // reset keys in case of reuse, prevent fail on cascade requirements
        cache.clear(); // clear cache in case of reuse
        return super.evaluate();
    }

    @Override
    public Class<? extends Converter<Boolean>> fetchConverter(Validate annotation) {
        return annotation.with();
    }

    @Override
    protected BinaryOperator<Boolean> evaluationReductor() {
        return (acc,val) -> acc && val;
    }

    @Override
    public Comparator<Method> comparingPredicate() {
        return Comparator.comparing((Method m) -> !getMainAnnotation(m).mandatory())
                .thenComparingInt(m -> getMainAnnotation(m).boundTo().length)
                .thenComparingInt(m -> getMainAnnotation(m).requires().length);
    }

//    @Override
    protected Function<Method, Boolean> evaluateAlgorithm() {
        return null;//invokeOnNull(m -> !validateMethod(m) || (checkRequirements(m) && checkConflicts(m)), this::validateAlternatives);
    }

    /**
     * <p>Validate method with a generic validate interface<p/>
     *
     *
     * @param v Annotation carrying all the validation information
     * @param method target method to evaluate
     * @return true if method return a valid value
     * @throws Exception if not valid
     */
    protected Boolean evaluateMethod(Validate v, Method method) throws Exception {
        Constraint validator = Converter.create(v.with());

        validator.setContext(contexts);
        validator.setIgnoreContext(ignoreContexts);

        return validator.evaluate(ArrayUtils.addAll(new Object[]{fetchValue(method)}, fetchValues(v.boundTo())));
    }

    /**
     * <p>Extends {@link AbstractEvaluator#invokeOnNull}, providing handler for andromeda's custom Exceptions</p>
     *
     * @param throwingFunction a function capable of throwing exceptions
     * @param fallback         a function to call in case of failure
     * @param <R>              a return type
     * @return the result of the evaluation of throwing function or fallback
     * @throws InvalidFieldException if evaluation non-null but invalid
     */
    @Override
    protected <R> Function<Method, R> invokeOnNull(FunctionalWrapper<Method, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException {
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

    @Override
    protected <R> Function<Method, R> invokeOnNull(FunctionalWrapper<Method, R, Exception> throwingFunction, Function<Method, R> fallback) throws InvalidFieldException {
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
                throw new RuntimeException(e);
            }
        };
    }

    /* ----------------- PRIVATE METHODS ----------------- */

    private ValidationState check(Method method) throws InvalidFieldException {
        return Optional.ofNullable(av.get(method.getName())).orElse(ValidationState.NOT_YET_EVALUATED);
    }

    /**
     * <p>Short hand for {@link #evaluateMethod(Validate, Method)} uses method own validate annotation</p>
     */
    private Boolean validateMethod(Method method) throws Exception {
        if (check(method) == ValidationState.VALID)
            return true;
        if (check(method) == ValidationState.NOT_YET_EVALUATED)
            av.put(method.getName(), ValidationState.ON_EVALUATION);
        return !evaluateMethod(getMainAnnotation(method), method) || (checkRequirements(method) && checkConflicts(method));
    }

    /**
     * @param method a mandatory method which returned a null value
     * @return true if at least one of the alternatives is valid or mandatory is ignorable
     * @throws InvalidFieldException if no non-null alternatives are found or alternatives are ignored
     */
    private boolean validateAlternatives(Method method) throws InvalidFieldException {
        av.put(method.getName(), ValidationState.NOT_SET);
        Validate ann = getMainAnnotation(method);

        if (isIgnorable(Validate.Ignore.MANDATORY)
                || !ann.mandatory()
                || Arrays.stream(ann.context()).anyMatch(ctx -> ignoreContexts.contains(ctx))
                || (!contexts.isEmpty() && Arrays.stream(ann.context()).noneMatch(ctx -> contexts.contains(ctx))))
            return true;

        if (isIgnorable(Validate.Ignore.ALTERNATIVES) || ann.alternatives().length == 0)
            throw new InvalidFieldException(method);

        return validateAlternatives(method, ann);
    }

    private boolean validateAlternatives(String... alternatives) throws NoAlternativeException {
        if(alternatives.length > 0 && Arrays.stream(fetchValues(alternatives)).anyMatch(Objects::isNull))
           throw new NoAlternativeException(Arrays.stream(alternatives).reduce("", (acc, val) -> acc + ", " + val));
        return true;
    }

    /**
     * <p>Retrieve conflictual methods with one passed as argument</p>
     *
     * @param m method with possible conflicts
     * @return true if no conflicts found
     * @throws ConflictFieldException if conflicts are found
     */
    private Boolean checkConflicts(Method m) throws ConflictFieldException {
        Validate ann = getMainAnnotation(m);
        if (!isIgnorable(Validate.Ignore.CONFLICTS)
                && List.of(ann.conflicts())
                .stream()
                .map(FunctionalUtils.tryCatch(mName -> new PropertyDescriptor(mName, this.t.getClass()).getReadMethod()))
                .map(invokeOnNull(method -> validateChildMethod(ann, method), () -> false))
                .filter(valid -> valid)
                .findFirst().orElse(false)) throw new ConflictFieldException(m, List.of(ann.conflicts()));
        av.put(m.getName(), ValidationState.VALID);
        return true;
    }

    private boolean checkConflicts(String ...conflicts) {
        if(Arrays.stream(fetchValues(conflicts))
                .allMatch(Objects::isNull))
            throw new ConflictFieldException(Arrays.stream(conflicts).reduce((acc, val) -> acc + ", " + val) + " are conflictual");

        return true;
    }

    /**
     * <p>Requirements must be met by parent mandatory annotations and child annotation of a mandatory parent</p>
     *
     * @param method with requirements to satisfy
     * @return true if all requirements are met
     * @throws RequirementsException if some requirements are not met
     */
    private Boolean checkRequirements(Method method) throws RequirementsException, InvalidFieldException {
        if (isIgnorable(Validate.Ignore.REQUIREMENTS)) // if method already validated return true
            return true;

        Validate ann = getMainAnnotation(method);
        av.put(method.getName(), ValidationState.ON_EVALUATION);

        // check requirements for field
        if (List.of(ann.requires())
                .stream()
                // create a stream of require methods (not null)
                .map(FunctionalUtils.tryCatch(mName -> new PropertyDescriptor(mName, this.t.getClass()).getReadMethod()))
                // try to validate methods with their own annotation
                .map(invokeOnNull(m -> !validateChildMethod(ann, m) || checkChildRequirements(m) && checkChildConflicts(m), this::validateChildAlternatives))
                .filter(valid -> !valid)
                .findFirst().orElse(true)) return true;
        throw new RequirementsException(method, List.of(ann.requires()));
    }

    // check props are instatiated and load in cache
    private Boolean checkRequirements(String ...requiredProperties) {
        if(requiredProperties.length > 0 && Arrays.stream(fetchValues(requiredProperties))
                .anyMatch(Objects::isNull))
            throw new RequirementsException(Arrays.stream(requiredProperties).reduce((acc,val) -> acc + ", " + val) + " are required");

        return true;
    }

    /**
     * <p>{@link #evaluateMethod(Validate, Method)} wrapper but use a fallback annotation if not annotated</p>
     */
    private Boolean validateChildMethod(Validate v, Method method) throws Exception {
        return isAssessable(method.getName()) || evaluateMethod(Optional.ofNullable(getMainAnnotation(method)).orElse(v), method);
    }

    private boolean validateChildAlternatives(Method method) throws InvalidFieldException {
        if (check(method) == ValidationState.VALID) // if method already validated return true
            return true;

        Validate ann = getMainAnnotation(method);

        if (isIgnorable(Validate.Ignore.ALTERNATIVES))
            throw new InvalidFieldException(method);

        return validateAlternatives(method, ann);
    }

    /**
     * <p>Shorthand for {@link #checkConflicts(Method)}, in most case, required or conflictual object are not annotated</p>
     * this means that they have no specific conflicts or have same as parent
     */
    private Boolean checkChildConflicts(Method method) throws RequirementsException {
        return getMainAnnotation(method) == null || checkConflicts(method);
    }

    /**
     * <p>Shorthand for {@link #checkRequirements(Method)}, in most case, required or conflictual object are not annotated</p>
     * this means that they have no specific requirements or have same as parent
     */
    private Boolean checkChildRequirements(Method method) throws RequirementsException {
        if (av.containsKey(method.getName()) && av.get(method.getName()).equals(ValidationState.ON_EVALUATION))
            throw new CyclicRequirementException("Detected cyclic dependency with " + method.getName());
        return getMainAnnotation(method) == null || checkRequirements(method);
    }

    private boolean isIgnorable(Validate.Ignore ignorable) {
        return ignoreList.contains(ignorable);
    }

    /**
     * @return true if the property hasn't been already evaluated or it is valid
     */
    private boolean isAssessable(String propertyName) {
        return av.containsKey(propertyName) && !(av.get(propertyName).equals(ValidationState.NOT_SET) || av.get(propertyName).equals(ValidationState.ON_EVALUATION));
    }

    private String displayName(String method) {
        return Stream.of(method).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining());
    }

    private boolean validateAlternatives(Method method, Validate ann) {
        return Arrays.stream(ann.alternatives())
                .map(FunctionalUtils.tryCatch(mName -> new PropertyDescriptor(mName, this.t.getClass()).getReadMethod()))
                .map(invokeOnNull(
                        m -> !validateChildMethod(ann, m) || (checkChildRequirements(m) && checkChildConflicts(m)),
                        m -> false))
                // lazy find first valid
                .filter(valid -> valid)
                .findFirst().orElseThrow(() -> new NoAlternativeException(method, List.of(ann.alternatives())));
    }

    @Override
    protected Boolean process(Validate v, Converter<Boolean> converter, Object prop, Method target) throws Exception {
        av.put(target.getName(), ValidationState.ON_EVALUATION);
        try {
            return !super.process(v, converter, prop, target) || (checkRequirements(v.requires()) && checkConflicts(v.conflicts()));
        } catch (NullPointerException npe) { // check alternatives
            System.out.println("validating alternatives");
            try {
                return validateAlternatives(v.alternatives());
            } catch (NoAlternativeException nae) {
                throw new NoAlternativeException(target, Arrays.asList(v.alternatives().clone()));
            }
        } catch (InvalidCollectionFieldException e) {
            throw new InvalidFieldException("Collection " + displayName(target.getName()) + "[] : " + e.getMessage());
        } catch (InvalidPropertiesFormatException | InvalidFieldException | InvalidNestedFieldException e) {
            throw new InvalidFieldException(displayName(target.getName()) + e.getMessage());
        } catch (ConflictFieldException e) {
            throw new ConflictFieldException(e.getMessage());
        } catch (CyclicRequirementException e) {
            throw new CyclicRequirementException(e.getMessage());
        } catch (RequirementsException e) {
            throw new RequirementsException(displayName(target.getName()) + " : " + e.getMessage());
        } catch (NoAlternativeException nae) {
            throw new NoAlternativeException(displayName(target.getName()) + " : " + nae.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

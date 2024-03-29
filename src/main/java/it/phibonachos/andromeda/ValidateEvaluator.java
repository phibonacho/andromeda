package it.phibonachos.andromeda;

import it.phibonachos.andromeda.exception.*;
import it.phibonachos.andromeda.types.Constraint;
import it.phibonachos.ponos.AbstractEvaluator;
import it.phibonachos.ponos.converters.Converter;
import it.phibonachos.ponos.converters.ConverterException;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidateEvaluator<Target> extends AbstractEvaluator<Target, Boolean, Validate, InvalidFieldException> implements Validator {
    final private Map<String, ValidationState> av;
    private Set<Validate.Ignore> ignoreList;
    private Set<String> contexts, ignoreContexts;

    private enum ValidationState {VALID, NOT_SET, ON_EVALUATION, NOT_YET_EVALUATED}

    public ValidateEvaluator(Target t) {
        super(t);
        this.annotationClass = Validate.class;
        av = new HashMap<>();
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
        if(ignorable != null)
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

    public Boolean validate() throws Exception {
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

    /* ----------------- PRIVATE METHODS ----------------- */
    private boolean validateAlternatives(String property, String... alternatives) throws NoAlternativeException {
        if (isIgnorable(Validate.Ignore.ALTERNATIVES) || alternatives.length == 0)
            throw new InvalidFieldException("");

        if(Arrays.stream(fetchValues(alternatives)).allMatch(Objects::isNull))
            throw new NoAlternativeException(property, List.of(alternatives));

        return true;
    }

    private boolean checkConflicts(String propertyName, boolean skipWhenUnset, String ...conflicts) {
        if(!(av.get(propertyName) == ValidationState.NOT_SET && skipWhenUnset)
        && conflicts.length > 0 && Arrays.stream(fetchValues(conflicts))
                .noneMatch(Objects::isNull))
            throw new ConflictFieldException(propertyName, List.of(conflicts));

        return true;
    }

    // check props are instantiated and load in cache
    private Boolean checkRequirements(String propertyName, boolean skipWhenUnset, String ...requiredProperties) {
        if(!(av.get(propertyName) == ValidationState.NOT_SET && skipWhenUnset)
                && requiredProperties.length > 0 && Arrays.stream(fetchValues(requiredProperties))
                .anyMatch(Objects::isNull))
            throw new RequirementsException(propertyName, List.of(requiredProperties));

        return true;
    }

    private boolean isIgnorable(Validate.Ignore ignorable) {
        return ignoreList.contains(ignorable);
    }

    private String displayName(String method) {
        return Stream.of(method).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining());
    }

    private boolean auxProcess(Validate v, Constraint converter, Object prop, Method target, boolean skipWhenUnset) throws Exception {
        try {
            converter.setContext(this.contexts);
            converter.setIgnoreContext(this.ignoreContexts);
            return converter.evaluate(ArrayUtils.addAll(new Object[]{prop}, fetchValues(v.boundTo())));
        } catch (NullPointerException npe) {
            av.put(target.getName(), ValidationState.NOT_SET);

            if (skipWhenUnset)
                return true;

            if (isIgnorable(Validate.Ignore.ALTERNATIVES) || v.alternatives().length == 0)
                throw new InvalidFieldException(target.getName(), List.of(v.alternatives()));

            return validateAlternatives(target.getName(), v.alternatives());
        } catch (InvalidFieldException ife) {
            if (skipWhenUnset)
                return true;

            throw ife;
        }
    }

    @Override
    protected Boolean process(Validate v, Converter<Boolean> converter, Object prop, Method target) throws Exception {
        av.put(target.getName(), ValidationState.ON_EVALUATION);
        try {
            // An unset property should be considered valid whether:
            // the property is not set
            // the property belong to an ignorable context
            // the property do not belong to an evaluated context
            // the mandatory clause is ignored
            boolean swu = !v.mandatory()
                    || this.skipIgnoreContext(v)
                    || (!this.contexts.isEmpty()
                        && this.skipNotContext(v))
                    || isIgnorable(Validate.Ignore.MANDATORY);

            // check property against its validator
            boolean valid = !auxProcess(v, (Constraint) converter, prop, target, swu)
                    || (checkRequirements(target.getName(), swu, v.requires()) && checkConflicts(target.getName(), swu, v.conflicts()));

            av.put(target.getName(), ValidationState.VALID);

            return valid;

        } catch (InvalidCollectionFieldException e) {
            throw new InvalidFieldException("Collection " + displayName(target.getName()) + "[] : " + e.getMessage());
        } catch (InvalidPropertiesFormatException | ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean skipIgnoreContext(Validate v) {
        return Arrays.stream(v.context()).anyMatch(ctx -> this.ignoreContexts.contains(ctx));
    }

    private boolean skipNotContext(Validate v) {
        return Arrays.stream(v.context()).noneMatch(ctx -> this.contexts.contains(ctx));
    }
}

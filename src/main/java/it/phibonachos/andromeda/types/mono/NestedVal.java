package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.types.SoloConstraint;

/**
 * <p>This class provides a handful way to propagate validation on nested objects.
 * A new validator will be instantiated to validate the child object.</p>
 *
 * @param <T> Generic class to be validated
 */
public class NestedVal<T> extends SoloConstraint<T> {
    private String nestedError = "";

    @Override
    public Boolean validate(T guard) throws Exception {
        try {
            return new ValidateEvaluator<>(guard)
                    .onlyContexts(this.context)
                    .ignoreContexts(this.ignoreContext)
                    .validate();
        } catch(Exception e){
            this.nestedError = e.getMessage();
            return false;
        }
    }

    @Override
    public String message(){
        return this.nestedError + "[nested]";
    }
}
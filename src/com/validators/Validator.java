package com.validators;

import com.annotation.Validate;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Validator<Target> extends AbstractValidator <Target, Boolean> {
    public Validator(Target t) {
        super(t);
    }

    @Override
    public Boolean validate() throws Exception{
        return Arrays.stream(this.t.getClass().getDeclaredMethods())
                .filter(m -> Optional.ofNullable(m.getAnnotation(Validate.class))
                        .map(Validate::mandatory)
                        .orElse(false))
                .filter(m -> m.getParameterCount() == 0)
                .map(invokeAndCatch(m -> invokeValidate(m.getAnnotation(Validate.class), m)))
                .filter(bool -> !bool).findFirst().orElse(true);
    }

    @SuppressWarnings("unchecked") // giusto perché mi fa schifo vederlo tutto giallo...
    // questa funzione dovrebbe essere implementata come nella parte commentata, in modo da poter lanciare direttamente l'eccezione
    private Boolean invokeValidate(Validate v, Method paramGetter) throws Exception {
        return v.value().getDeclaredConstructor().newInstance()
                .validate(paramGetter.invoke(this.t));
    }

    @FunctionalInterface
    private interface ThrowingFunction<I, R, E extends Exception> {
        R accept(I s) throws E;
    }

    // troppo specifico...
    private Function<? super Method, Boolean> invokeAndThrow(ThrowingFunction<Method, Boolean, Exception> throwingFunction) throws RuntimeException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                Validate ann = i.getAnnotation(Validate.class);
                if(ann.mandatory()) // controlla alternative
                    throw new RuntimeException(i.getName().replaceAll("^(get|is|has)", "").toLowerCase() + " cannot be null" + (ann.orAtLeast().length > 0 ? ", viable alternatives are: "+ Arrays.stream(ann.orAtLeast()).map(name -> name.replaceAll("^(get|is|has)", "")).reduce((s1, s2) ->s1 +", "+s2).orElse("") : ""));
                return true; // se è null è valido
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Function<? super Method, Boolean> invokeAndCatch(ThrowingFunction<Method, Boolean, Exception> throwingFunction) throws Exception {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                Validate ann = i.getAnnotation(Validate.class);
                System.out.println(i.getName() + " is " + (ann.mandatory() ? " " : "not ") + "mandatory");
                System.out.println(i.getName() + " has " + ann.orAtLeast().length + " alternatives");
                if(ann.mandatory() && ann.orAtLeast().length == 0) // controlla alternative
                    throw new RuntimeException(i.getName().replaceAll("^(get|is|has)", "").toLowerCase() + " cannot be null and has no viable alternatives" );
                List<Method> orElse = Arrays.stream(ann.orAtLeast()).map(checkMethod(mName ->  this.t.getClass().getDeclaredMethod(mName))).collect(Collectors.toList());
                return orElse.stream()
                        .map(invokeAndThrow(method -> ann.value().getDeclaredConstructor().newInstance()
                                .validate(method.invoke(this.t))))
                        .filter(valid -> valid)
                        .findFirst().orElseThrow(()->new RuntimeException(i.getName().replaceAll("^(get|is|has)", "").toLowerCase() + " cannot be null" + (ann.orAtLeast().length > 0 ? ", viable alternatives are: "+ Arrays.stream(ann.orAtLeast()).map(name -> name.replaceAll("^(get|is|has)", "")).reduce((s1, s2) ->s1 +", "+s2).orElse("") : "")));// valido le alternative
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<? super String, Method> checkMethod(ThrowingFunction<String, Method, NoSuchMethodException> throwingFunction){
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NoSuchMethodException nsme) {
                throw new RuntimeException("cannot find method: " + nsme.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }

}

package it.phibonachos.andromeda.types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public interface Constraint<ControlType> {
    static <C, T extends Constraint<C>> T create(Class<T> vti) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return vti.getConstructor().newInstance();
    }

    <Target> ControlType evaluate(Target target, Method... props) throws Exception;

    void setContext(Set<String> context);

    void setIgnoreContext(Set<String> ignoreContext);
}

package it.phibonachos.processor;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.MultiConstraint;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidateProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Validate.class);

        for(Element e : elements){
            if(!e.getKind().equals(ElementKind.METHOD)) {
                error(e, "%s %s cannot be annotated with @%s", e.getKind().name(), e.getSimpleName(), Validate.class.getSimpleName());
                continue;
            }

            Validate validate = e.getAnnotation(Validate.class);
            Class<? extends MultiConstraint> mvc = validate.with();
            List<Method> validationMethod = Arrays.stream(mvc.getMethods()).filter(method -> method.getName().equals("validate")).collect(Collectors.toList());

            if(validationMethod.size() < 1) {
                error(e, "%s do not provide any validate(args...) method", mvc.getCanonicalName());
                continue;
            } else if(validationMethod.size() > 1) {
                error(e, "%s should provide only one validate(args...) method", mvc.getCanonicalName());
                continue;
            }

            int arity = validationMethod.get(0).getParameterCount();
            int bounded = validate.boundTo().length;
            if(arity != bounded) {
                error(e, "%s provides validate(args...) with %s arguments but %s boundTo clause provides %s properties", mvc.getSimpleName(), arity, e.getSimpleName(), bounded);
                continue;
            }



        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Validate.class.getCanonicalName());
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element e, String msg, Object ...args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private void warn(Element e, String msg, Object ...args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, String.format(msg, args), e);
    }

    private void note(Element e, String msg, Object ...args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(msg, args), e);
    }
}

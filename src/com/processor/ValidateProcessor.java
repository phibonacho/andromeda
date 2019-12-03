package com.processor;


import com.annotation.validate.Validate;
import com.annotation.validate.ValidateEvaluator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("com.annotation.validate.Validate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ValidateProcessor extends AbstractProcessor {

    public ValidateProcessor() {}

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {//For each element annotated with the Handleable annotation
        System.err.println("starting processing validation annotation: ");
        for (Element e : roundEnv.getElementsAnnotatedWith(Validate.class)) {

            // Check if the type of the annotated element is not a field. If yes, return a warning.
            if (e.getKind() != ElementKind.METHOD) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.WARNING,
                        "Not a method", e);
                continue;
            }

            //Generate a source file with a specified class name.
            try {
                ValidateEvaluator validator = new ValidateEvaluator<>(e);
                assert validator.evaluate();
            } catch (Exception x) {
                System.out.println("experienced error!");
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        x.toString());
                return false;
            }
        }return true;
    }

}

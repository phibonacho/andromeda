package com.validators;

public abstract class AbstractValidator <Target, Control, Annotation> {
    protected Target t;
    protected Class<Annotation> annotationClass;

    public AbstractValidator(Target t){
        this.t = t;
    }

    public abstract Control validate() throws Exception;
}
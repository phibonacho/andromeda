# Andromeda
Constraint validation annotations.

Andromeda is an object validation framework based on [Ponos](https://rollingflamingo.github.io/ponos), it consumes 
@Validate annotations and provide a boolean result in case of success or throws an exception in case of failure.

## @Validate
Validate annotation targets **getter methods** and states which object's properties should be validated 
and provides methods to customize each property validation.

### With
This clause provides the validation procedure defined as [MultiValueConstraint](#Validation Classes)'s extension.

```java
// Example
public class SomeClass {
    /*...*/

    @Validate // This uses NotNullValue classe, which only checks value is not null (uh?)
    public int someGetter() {
        /* */
    }

    @Validate(with = PositiveNum.class) // This uses the OOB PositiveNum class which check property is > 0
    public int someOtherGetter() {
        /* */
    }
    
    /*...*/
}
```

### Mandatory
This clause states whether the annotated property is mandatory or not. Mandatory property implicitly demand to be not null.
```java
// Example
public class SomeClass {
    /*...*/

    @Validate(with = PositiveNum.class) // This property is not mandatory
    public int someGetter() {
        /* */
    }

    @Validate(with = PositiveNum.class, mandatory = true) // This property is mandatory
    public int someOtherGetter() {
        /* */
    }
    
    /*...*/
}
```

### Alternatives
This clause indicates which object properties can be used when annotated property is not set.
Alternative property inherit parent validation class if not provided.
```java
// Example
public class SomeClass {
    /*...*/

    @Validate(with = PositiveNum.class, alternatives = "otherProperty") // This property will use otherProperty as fallback
    public int getProperty() {
        /* */
    }

    // This property will use PositiveNum as validation class
    public int getOtherProperty() {
        /* */
    }
    
    /*...*/
}
```
### Conflicts
This clause indicates which object properties conflicts with the annotated property, validation will halt throwing a ConflictException.
```java
// Example
public class SomeClass {
    /*...*/

    @Validate(with = PositiveNum.class, conflicts = "otherProperty") // This property will clash with otherProperty if set
    public int getProperty() {
        /* */
    }

    public int getOtherProperty() {
        /* */
    }
    
    /*...*/
}
```
### Requires
This clause defines property dependencies, this clause won't check for requirements validity, but only for them instantiation, if not validation will halt throwing a RequirementException.
```java
// Example
public class SomeClass {
    /*...*/

    @Validate(with = PositiveNum.class, requires = "otherProperty") // This property requires otherProperty to be set
    public int getProperty() {
        /* */
    }
    
    public int getOtherProperty() {
        /* */
    }
    
    /*...*/
}
```
### BoundTo
In case of complex validations, this method can be used to indicate which properties are involved together with annotated one.

```java
// Example
public class SomeClass {
    /*...*/

    @Validate(with = ComplexConstraint.class, boundTo = "otherProperty") 
    public int getProperty() {
        /* */
    }

    public int getOtherProperty() {
        /* */
    }

    /*...*/
}
```
### Contexts
This method can be used to indicate to which contexts the annotated property belong.


##Validation Classes
Andromeda relies on Ponos implementation, and so on its Converters system.

Long story short: a Ponos's ```Converter<T>``` expose a ```T evaluate(Object ...props)``` method, which (guess what), convert given properties to target T type.

In Andromeda the Converter interface is implemented by the MultiValueConstraint object, which handle the properties validation and contexts management.
All validation classes should extend MultiValueConstraint, or more easily an arity fix Constraint.

### Arity-fixed Constraints
Andromeda ships with arity-fixed constraint abstract classes out of the box, an arity fixed utility classes have from 1 to 8 parametric types and expose ```validate(T t, ...)``` method with matching arity.
Parametric types and arguments respect the properties to be validated positionally.

- SoloConstraint
- DuetConstraint
- TripletConstraint
- QuartetConstraint
- QuintetConstraint
- SextetConstraint
- SeptetConstraint
- OctetConstaint

SoloConstraint is the one of the eight fixed arity validation classes, and it provides a simple way to define a validation class which must evaluate a single property.
Imagine you have a String property that must have a capital letter at the beginning, you can define a constraint class as follows:

```java

import it.phibonachos.andromeda.types.SoloConstraint;

public class CapitalizeConstraint extends SoloConstraint<String> {
    @Override
    public Boolean validate(String guard) {
        return guard.matches('^[A-Z]');
    }
}

```

The parametric type defined in SoloConstraint defines the type of the target property to be validated, you can also define a custom message in case of failure:

```java

import it.phibonachos.andromeda.types.SoloConstraint;

public class CapitalConstraint extends SoloConstraint<String> {
    @Override
    public Boolean validate(String guard) {
        return guard.matches('^[A-Z]');
    }

    @Override
    public String message() {
        return "Do not start with a capital letter! :(";
    }
}

```


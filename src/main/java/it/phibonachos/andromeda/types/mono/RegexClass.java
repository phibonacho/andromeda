package it.phibonachos.andromeda.types.mono;

/**
 * <p>This class provides a template to validate strings using regex.</p>
 */
public abstract class RegexClass extends StringConstraint {

    /**
     * @return The regex to match.
     */
    public abstract java.lang.String regex();

    @Override
    public Boolean validate(java.lang.String guard) {
        return super.validate(guard) && guard.matches(regex());
    }

    @Override
    public java.lang.String message() {
        return "Do not match regex: " + regex();
    }
}

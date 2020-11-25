package it.phibonachos.andromeda.types.mono;

/**
 * <p>This class provides a template to validate strings using regex.</p>
 */
public abstract class RegexClass extends StringValue {

    /**
     * @return The regex to match.
     */
    public abstract String regex();

    @Override
    public Boolean validate(String guard) {
        return super.validate(guard) && guard.matches(regex());
    }

    @Override
    public String message() {
        return "Do not match regex: " + regex();
    }
}

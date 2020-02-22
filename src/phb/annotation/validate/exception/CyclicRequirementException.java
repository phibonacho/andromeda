package phb.annotation.validate.exception;

public class CyclicRequirementException extends RuntimeException {
    public CyclicRequirementException(String message) {
        super(message);
    }
}

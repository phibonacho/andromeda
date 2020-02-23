package phb.annotation.validate.exception;

public class CyclicRequirementException extends RequirementsException {
    public CyclicRequirementException(String message) {
        super(message);
    }
}

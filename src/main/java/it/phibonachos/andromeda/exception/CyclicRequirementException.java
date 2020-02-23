package it.phibonachos.andromeda.exception;

public class CyclicRequirementException extends RequirementsException {
    public CyclicRequirementException(String message) {
        super(message);
    }
}

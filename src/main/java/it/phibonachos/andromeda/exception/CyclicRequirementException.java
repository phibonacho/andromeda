package it.phibonachos.andromeda.exception;

import it.phibonachos.ponos.converters.ConverterException;

public class CyclicRequirementException extends ConverterException {
    public CyclicRequirementException(String message) {
        super(message);
    }
}

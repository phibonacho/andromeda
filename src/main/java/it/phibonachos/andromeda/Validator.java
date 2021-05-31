package it.phibonachos.andromeda;

public interface Validator {
    Boolean validate() throws Exception;

    Validator ignoreClauses(Validate.Ignore ...clauses);

    Validator ignoreContexts(String ...contexts);

    Validator onlyContexts(String ...contexts);
}

package evaluators;


import evaluators.validate_evaluator_classes.*;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class BoundToClauseTest {

    /* POSITIVE TEST */

    @Test
    public void successfulCompoundConstraint() {
        CompoundConstraintObject cco = new CompoundConstraintObject();
        ValidateEvaluator<CompoundConstraintObject> ve = new ValidateEvaluator<>(cco);
        cco.setProp("this is mandatory");
        cco.setProp1(true);
        cco.setProp2("this is required since prop1 is true");

        assert ve.validate();

        try {
            cco.setProp1(false);
            ve.validate();
        } catch (Exception ife) {
            assert ife instanceof InvalidFieldException;
            assert ife.getMessage().contains("incompatible values");
        }

        cco.setProp2(null);

        assert ve.validate();
    }

    /* NEGATIVE TEST */

}
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
public class AlternativesClauseTest {

    /* POSITIVE TEST */

    @Test
    public void validationAlternative() throws Exception {
        AlternativeObject ao = new AlternativeObject();
        ao.setAlternativeProp("this is a valid alternative string");

        assert new ValidateEvaluator<>(ao).validate();
    }

    /* NEGATIVE TEST */

    @Test
    public void validationAlternativeFail() {
        try {
            AlternativeObject ao = new AlternativeObject();
            assert new ValidateEvaluator<>(ao).validate();
        } catch (Exception e) {
            assert e instanceof NoAlternativeException;
            return;
        }
        assert false;
    }

    /**
     * Child method alternatives are discarded
     */
    @Test
    public void cascadeAlternativesFails() {
        try {
            CascadeAlternativeObject cao = new CascadeAlternativeObject();
            cao.setAProp1("at least this must be set");

            assert new ValidateEvaluator<>(cao).validate();
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getSimpleName());
            assert e instanceof NoAlternativeException;
            return;
        }
        assert false;
    }

}
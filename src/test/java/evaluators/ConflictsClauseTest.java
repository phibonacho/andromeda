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
public class ConflictsClauseTest {

    /* POSITIVE TEST */

    @Test
    public void validationWithConflicts() throws Exception {
        ConflictsObject co = new ConflictsObject();
        co.setConflictProp("this property pass validation as its conflicts is not set");

        assert new ValidateEvaluator<>(co).validate();
    }

    /* NEGATIVE TEST */

    @Test
    public void validationWithConflictsFail() {
        try {
            ConflictsObject co = new ConflictsObject();
            co.setProp("this prop conflicts with conflictProp");
            co.setConflictProp("this prop conflict with prop");
            assert new ValidateEvaluator<>(co).validate();
        } catch (Exception e) {
            assert e instanceof ConflictFieldException;
            return;
        }
        assert false;
    }
}
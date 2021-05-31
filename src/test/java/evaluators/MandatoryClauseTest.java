package evaluators;


import evaluators.validate_evaluator_classes.*;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MandatoryClauseTest {

    /* POSITIVE TEST */

    @Test
    public void plainValidation() throws Exception {
        SimpleObject so = new SimpleObject();

        so.setProp("this is a valid string as it is not blank");
        so.setProp2("this is also a valid string as it is not blank");

        assert new ValidateEvaluator<>(so).validate();
    }

    @Test
    public void contextPlainValidation() throws Exception {
        SimpleObject so = new SimpleObject();
        so.setProp("this is a valid string as it is not blank");
        ValidateEvaluator<SimpleObject> ve = new ValidateEvaluator<>(so);

        assert ve.onlyContexts("ctx1").validate();
        assert ve.onlyContexts().ignoreContexts("ctx2").validate(); // this should be equivalent

        so.setProp(null);
        so.setProp2("this is a mandatory field");

        assert ve.onlyContexts("ctx2").validate();
        assert ve.onlyContexts().ignoreContexts("ctx1").validate(); // this should be equivalent
    }

    /* NEGATIVE TEST */

    @Test
    public void plainValidationFail() {
        try {
            SimpleObject so = new SimpleObject();
            assert new ValidateEvaluator<>(so).validate();
        } catch (Exception ife) {
            assert ife instanceof InvalidFieldException;
            return;
        }
        assert false;
    }
}
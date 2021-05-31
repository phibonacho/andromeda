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
public class RequiresClauseTest {

    /* POSITIVE TEST */

    @Test
    public void validationWithRequirements() throws Exception {
        RequirementsObject ro = new RequirementsObject();
        ro.setProp("this property require another field initialized to be parsed as valid");
        ro.setRequiredProp(12d);

        assert new ValidateEvaluator<>(ro).validate();
    }

    @Test
    /* field not in the defined context are not validated at all */
    /* only context should operate as ignore? who knows...  */
    public void contextualRequirements() throws Exception {
        CascadeRequirementsObject cro = new CascadeRequirementsObject();
        ValidateEvaluator<CascadeRequirementsObject> evaluator = new ValidateEvaluator<>(cro).ignoreContexts("ctx1");
        cro.setProp("this is a mandatory property"); // this is commented out as we are validating only fields in ctx2
        cro.setReq1("this is a field required from prop");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }


        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq2("this is a field required from req1");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq3("this is a field required from req2");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }
    }

    @Test
    /* when a context is ignored it must maintain its clausole except mandatory one*/
    public void ignorableMaintainRequirements() throws Exception {
        CascadeRequirementsObject cro = new CascadeRequirementsObject();
        ValidateEvaluator<CascadeRequirementsObject> evaluator = new ValidateEvaluator<>(cro)
                .onlyContexts("ctx1") // this avoid ctx2 from being validated
                .ignoreContexts("ctx1"); // this turns properties defined in ctx1 non mandatory

        assert evaluator.validate(); // as no prop is set, validation completes successfully

        cro.setProp("this is no longer a mandatory property");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException; // even if no longer mandatory, a property set without matching requirements results in error
        }
    }

    /* NEGATIVE TEST */

    @Test
    public void validationWithRequirementsFail() {
        try {
            FailRequirementsObject fro = new FailRequirementsObject();
            fro.setProp("this property require another field initialized to be parsed as valid");
            assert new ValidateEvaluator<>(fro).validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
            return;
        }
        assert false;
    }

    /**
     * Required nodes inherits parent mandatoriness
     */
    @Test
    public void cascadeRequirementsFails() {
        CascadeRequirementsObject cro = new CascadeRequirementsObject();
        ValidateEvaluator<CascadeRequirementsObject> evaluator = new ValidateEvaluator<>(cro);
        evaluator.ignoreContexts("ctx2"); // or else req1 is considered mandatory
        cro.setProp("this is a mandatory property");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getClass().getSimpleName());
            e.printStackTrace();
            assert e instanceof RequirementsException;
        }

        cro.setReq1("this is a field required from prop");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq2("this is a field required from req1");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq3("this is a field required from req2");

        try {
           assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }
    }

    @Test
    public void nonMandatoryWithRequirementFails() {
        NonMandatoryWithRequirements nmwr = new NonMandatoryWithRequirements();
        nmwr.setProp("ciao");
        ValidateEvaluator<NonMandatoryWithRequirements> evaluator = new ValidateEvaluator<>(nmwr);

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }
    }
}
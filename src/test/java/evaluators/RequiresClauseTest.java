package evaluators;


import evaluators.validate_evaluator_classes.*;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    public void ignorableMaintainRequirements() {
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

    @Test
    public void boundRequirements() {
        BoundRequirement br = new BoundRequirement();

        br.setProp("a mandatory prop");
        br.setBoundProp("another prop with bound validation values");
        br.setPropWithRequirements("a prop with requirements");

        try {
            new ValidateEvaluator<>(br).validate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
     * If a node n1 require a second one n2, n2 cannot have n1 as its own dependency
     */
    @Test
    public void cyclicRequirementsFails() {
        try {
            CyclicRequirementsObject cro = new CyclicRequirementsObject();
            cro.setProp("require prop1");
            cro.setProp1("require prop");
            assert new ValidateEvaluator<>(cro).validate();
        } catch (Exception e){
            assert e instanceof CyclicRequirementException;
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
        cro.setProp("this is a mandatory property");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
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
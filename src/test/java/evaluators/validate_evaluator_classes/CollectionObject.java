package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.collections.CollectionType;

import java.util.List;

public class CollectionObject {
    private List<SimpleObject> myPrivateList;

    @Validate(with = CollectionType.class, mandatory = true)
    public List<SimpleObject> getMyPrivateList() {
        return myPrivateList;
    }

    public void setMyPrivateList(List<SimpleObject> lso) {
        this.myPrivateList = lso;
    }
}

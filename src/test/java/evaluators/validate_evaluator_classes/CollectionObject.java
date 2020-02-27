package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.collections.BlandCollectionType;

import java.util.List;

public class CollectionObject {
    private List<SimpleObject> myPrivateList;

    @Validate(with = BlandCollectionType.class, mandatory = true)
    public List<SimpleObject> getMyPrivateList() {
        return myPrivateList;
    }

    public void setMyPrivateList(List<SimpleObject> lso) {
        this.myPrivateList = lso;
    }
}

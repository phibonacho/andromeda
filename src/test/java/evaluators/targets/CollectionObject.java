package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.collections.BlandCollectionType;
import it.phibonachos.andromeda.types.collections.StrictCollectionType;

import java.util.List;

public class CollectionObject {
    private List<SimpleObject> myPrivateList;
    private List<SimpleObject> optList;

    @Validate(with = BlandCollectionType.class, mandatory = true)
    public List<SimpleObject> getMyPrivateList() {
        return myPrivateList;
    }

    public void setMyPrivateList(List<SimpleObject> lso) {
        this.myPrivateList = lso;
    }

    @Validate(with = StrictCollectionType.class)
    public List<SimpleObject> getOptList() {
        return optList;
    }

    public void setOptList(List<SimpleObject> optList) {
        this.optList = optList;
    }
}

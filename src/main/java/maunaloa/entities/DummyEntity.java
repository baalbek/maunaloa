package maunaloa.entities;

import org.bson.types.ObjectId;

/**
 * Created by rcs on 5/11/14.
 */
public class DummyEntity implements MaunaloaEntity{
    //private ObjectId oid = new ObjectId("52f1494744aeff728437e6c9");
    private ObjectId oid = new ObjectId("52f1494744aeff728437e6c3");
    @Override
    public ObjectId getOid() {
        return oid;
    }
}

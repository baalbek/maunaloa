package maunaloa;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.04.14
 * Time: 11:34
 */
public class MaunaloaStatus {
    private int entityStatus;
    private int chartLineStatus;

    public MaunaloaStatus(int entityStatus, int chartLineStatus) {
        this.entityStatus = entityStatus;
        this.chartLineStatus = chartLineStatus;
    }
    public int getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(int entityStatus) {
        this.entityStatus = entityStatus;
    }
}

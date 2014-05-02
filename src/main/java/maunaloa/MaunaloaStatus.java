package maunaloa;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.04.14
 * Time: 11:34
 */
public class MaunaloaStatus {
    private int entityStatus;
    private final IntegerProperty chartLineStatusProperty;

    public MaunaloaStatus(int entityStatus, IntegerProperty chartLineStatusProperty) {
        this.entityStatus = entityStatus;
        this.chartLineStatusProperty = new SimpleIntegerProperty(StatusCodes.NA);

        if (chartLineStatusProperty != null) {
            this.chartLineStatusProperty.bind(chartLineStatusProperty);
        }
    }
    public int getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(int entityStatus) {
        this.entityStatus = entityStatus;
    }

    public int getChartLineStatus() {
        return chartLineStatusProperty.get();
    }

}

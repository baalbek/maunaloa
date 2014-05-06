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
    private IntegerProperty entityStatus;
    private final IntegerProperty chartLineStatusProperty;

    public MaunaloaStatus(IntegerProperty entityStatusProperty, IntegerProperty chartLineStatusProperty) {
        this.entityStatus = entityStatusProperty;
        this.chartLineStatusProperty = new SimpleIntegerProperty(StatusCodes.NA);

        if (chartLineStatusProperty != null) {
            this.chartLineStatusProperty.bind(chartLineStatusProperty);
        }
    }
    public int getEntityStatus() {
        return entityStatus.get();
    }

    public void setEntityStatus(int entityStatus) {
        this.entityStatus.set(entityStatus);
    }

    public int getChartLineStatus() {
        return chartLineStatusProperty.get();
    }

}

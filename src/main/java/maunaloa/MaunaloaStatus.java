package maunaloa;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import oahu.functional.Procedure0;
import oahu.functional.Procedure2;

import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.04.14
 * Time: 11:34
 */
public class MaunaloaStatus {
    private IntegerProperty entityStatus;
    private final IntegerProperty chartLineStatusProperty;

    public MaunaloaStatus(IntegerProperty entityStatusProperty,
                          IntegerProperty chartLineStatusProperty) {
        this.entityStatus = entityStatusProperty;
        this.chartLineStatusProperty = new SimpleIntegerProperty(StatusCodes.NA);

        if (chartLineStatusProperty != null) {
            this.chartLineStatusProperty.bind(chartLineStatusProperty);
        }
    }
    public MaunaloaStatus(IntegerProperty entityStatusProperty,
                          IntegerProperty chartLineStatusProperty,
                          Consumer<Integer> onEntityStatusChanged) {
        this(entityStatusProperty,chartLineStatusProperty);
        this.onEntityStatusChanged = onEntityStatusChanged;
    }
    public int getEntityStatus() {
        return entityStatus.get();
    }

    public void setEntityStatus(int entityStatus) {
        this.entityStatus.set(entityStatus);
        if (onEntityStatusChanged != null) {
            onEntityStatusChanged.accept(entityStatus);
        }
    }

    public int getChartLineStatus() {
        return chartLineStatusProperty.get();
    }


    //region Events
    private Consumer<Integer> onEntityStatusChanged;
    public void setOnEntityStatusChanged(Consumer<Integer> onEntityStatusChanged) {
        this.onEntityStatusChanged = onEntityStatusChanged;
    }
    //endregion Events

}

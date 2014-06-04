package maunaloa;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.04.14
 * Time: 11:34
 */
public class MaunaloaStatus {
    private final IntegerProperty entityStatus;
    private final IntegerProperty chartLineStatusProperty;
    private final BooleanProperty cleanProperty;

    public MaunaloaStatus(IntegerProperty entityStatusProperty,
                          IntegerProperty chartLineStatusProperty,
                          BooleanProperty cleanProperty) {
        this.entityStatus = entityStatusProperty;
        this.chartLineStatusProperty = new SimpleIntegerProperty(StatusCodes.NA);
        this.cleanProperty = new SimpleBooleanProperty(true);

        if (chartLineStatusProperty != null) {
            this.chartLineStatusProperty.bind(chartLineStatusProperty);
        }
        if (cleanProperty != null) {
            this.cleanProperty.bind(cleanProperty);
        }
    }
    public int getEntityStatus() {
        return entityStatus.get();
    }
    public int getChartLineStatus() {
        return chartLineStatusProperty.get();
    }
    @Override
    public String toString() {
        return String.format("Entity status: %d, chart line status: %d, is clean: %s",
                getEntityStatus(),
                getChartLineStatus(),
                cleanProperty.get()
                );
    }


    //region Utgått
    /*public MaunaloaStatus(IntegerProperty entityStatusProperty,
                          IntegerProperty chartLineStatusProperty,
                          Consumer<Integer> onEntityStatusChanged) {
        this(entityStatusProperty,chartLineStatusProperty);
        this.onEntityStatusChanged = onEntityStatusChanged;
    }*/

    /*public void setEntityStatus(int entityStatus) {
        this.entityStatus.set(entityStatus);
        if (onEntityStatusChanged != null) {
            onEntityStatusChanged.accept(entityStatus);
        }
    }*/


    //region Events
    /*private Consumer<Integer> onEntityStatusChanged;
    public void setOnEntityStatusChanged(Consumer<Integer> onEntityStatusChanged) {
        this.onEntityStatusChanged = onEntityStatusChanged;
    }*/
    //endregion Events

    //endregion Utgått
}

package maunaloa.views.table;


import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/17/12
 * Time: 10:52 AM
 */

class BooleanCell {

}
/*
class BooleanCell extends TableCell<ObservableList<DerivativeBean>, Boolean> {
    private CheckBox checkBox;

    public BooleanCell() {

        checkBox = new CheckBox();
        checkBox.setDisable(true);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(isEditing())
                    commitEdit(newValue == null ? false : newValue);
            }

        });
        this.setGraphic(checkBox);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);


    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }
        checkBox.setDisable(false);
        checkBox.requestFocus();

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        checkBox.setDisable(true);
    }

    public void commitEdit(Boolean value) {
        super.commitEdit(value);
        checkBox.setDisable(true);
    }


    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            checkBox.setSelected(item);
        }
    }
}
*/

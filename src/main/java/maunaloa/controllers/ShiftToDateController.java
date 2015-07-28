package maunaloa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import maunaloa.service.FxUtils;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Created by rcs on 27.07.15.
 *
 */
public class ShiftToDateController {
    @FXML private Button btnCancel;
    @FXML private Button btnOk;
    @FXML private DatePicker dpShift;
    private final Consumer<LocalDate> acceptFn;

    public ShiftToDateController(Consumer<LocalDate> fn) {
        acceptFn = fn;
    }
    public void initialize() {
        btnCancel.setOnAction(event -> {
            FxUtils.closeView(event);
        });
        btnOk.setOnAction(event -> {
            acceptFn.accept(dpShift.getValue());
            FxUtils.closeView(event);
        });
    }
}

package maunaloa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import maunaloa.entities.MaunaloaEntity;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.service.FxUtils;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by rcs on 5/10/14.
 */
public class CommentsController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    @FXML private Button btnCancel;
    @FXML private Button btnOk;
    @FXML private TextArea txComment;

    private final Consumer<CommentEntity> acceptFn;
    private final MaunaloaEntity entity;

    public CommentsController(MaunaloaEntity entity,
                              Consumer<CommentEntity> acceptFn) {
        this.acceptFn = acceptFn;
        this.entity = entity;
    }

    public void initialize() {
        btnCancel.setOnAction(event -> {
            FxUtils.closeView(event);
        });
        btnOk.setOnAction(event -> {
            acceptFn.accept(new CommentEntity(entity,txComment.getText(),LocalDateTime.now()));
            FxUtils.closeView(event);
        });
        txComment.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                acceptFn.accept(new CommentEntity(entity,txComment.getText(),LocalDateTime.now()));
                FxUtils.closeView(e);
            }
        });
    }
}

package maunaloa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import maunaloa.entities.windowdressing.CommentEntity;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by rcs on 5/10/14.
 */
public class CommentsController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    @FXML private Button btnCancel;
    @FXML private Button btnOk;

    private final Consumer<List<CommentEntity>> acceptFn;
    public CommentsController(Consumer<List<CommentEntity>> acceptFn) {
        this.acceptFn = acceptFn;
    }

}

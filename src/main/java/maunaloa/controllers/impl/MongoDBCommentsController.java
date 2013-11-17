package maunaloa.controllers.impl;

import com.mongodb.DBObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import maunaloa.controllers.MongoDBController;
import maunaloa.domain.MaunaloaContext;
import maunaloa.domain.TreeViewItemWrapper;
import maunaloa.utils.FxUtils;
import maunaloa.views.CanvasGroup;
import maunaloa.views.MongodbLine;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 30.10.13
 * Time: 11:39
 */
public class MongoDBCommentsController implements MongoDBController {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());

    @FXML
    private Button btnAddComment;
    @FXML
    private Button btnDeleteComment;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSave;
    @FXML
    private TreeView treeComments;
    @FXML
    private TextArea txaComment;

    private TreeItem<TreeViewItemWrapper> root;
    //private TreeItem<? super TreeViewItemWrapper> root2;

    private MaunaloaContext ctx;

    private List<String> existingComments;
    private List<String> newComments = new ArrayList<>();

    public void initialize() {
        root = new TreeItem<TreeViewItemWrapper>(new TreeViewItemWrapper("MongoDB Comments"));
        root.setExpanded(true);
        treeComments.rootProperty().set(root);

        //populateTree();

        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FxUtils.closeView(actionEvent);
            }
        });
        btnAddComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String newComment = txaComment.getText();
                TreeItem<TreeViewItemWrapper> item = new TreeItem<>(new TreeViewItemWrapper(newComment));
                TreeItem<TreeViewItemWrapper> selected = (TreeItem<TreeViewItemWrapper>)treeComments.getFocusModel().getFocusedItem();
                selected.getChildren().add(item);
                newComments.add(newComment);
            }
        });
    }

    private void populateTree() {
        /*
        List<String> comments = ctx.getFacade().getWindowDressingModel().fetchComments(ctx.getObjectId());
        for (String comment : comments) {
            root.getChildren().add(new TreeItem<>(comment));
        }
        //*/
        List<CanvasGroup> lines = ctx.getLines();
        if (lines != null) {
            for (CanvasGroup line : lines) {
                System.out.println("Line: " + line);
                MongodbLine mongoLine = line instanceof MongodbLine ? (MongodbLine)line : null;
                if (mongoLine == null) continue;
                TreeItem<TreeViewItemWrapper> item = new TreeItem<>(new TreeViewItemWrapper(mongoLine));
                List<DBObject> comments = ctx.getWindowDressingModel().fetchComments(mongoLine.getMongodbId());
                for (DBObject c : comments) {
                    TreeItem<TreeViewItemWrapper> citem = new TreeItem<>(new TreeViewItemWrapper(c));
                    item.getChildren().add(citem);
                }
                root.getChildren().add(item);
            }
        }
    }

    @Override
    public void setContext(MaunaloaContext ctx) {
        this.ctx = ctx;
        populateTree();
    }

    /*
    private void testThis() {
        TreeView view = new TreeView();

        TreeItem<? super TreeViewItemWrapper> item = new TreeItem<>(new TreeViewItemWrapper("sdfs"));

        TreeItem<? super TreeViewItemWrapper> item2 = new TreeItem<>(new TreeViewItemWrapper("sdfs"));
        item.getChildren().add(item2);

        List<? super Number> lx = new ArrayList<>();
        lx.add(3);
        lx.add(new Double("343.3"));

        root.getChildren().add(new TreeItem<TreeViewItemWrapper>(new TreeViewItemWrapper("sfsd")));

        root2.getChildren().add(new TreeItem<>(new TreeViewItemWrapper("sfs")));
    }
    //*/
}

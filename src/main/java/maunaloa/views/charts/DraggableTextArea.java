package maunaloa.views.charts;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 14.05.14
 * Time: 11:04
 */
public class DraggableTextArea {
    private Circle anchor;
    private TextArea textArea;
    private static double MY_WIDTH = 230;

    public DraggableTextArea(String text, double x, double y) {
        this(text,x,y,MY_WIDTH);
    }
    public DraggableTextArea(String text,
                             double x,
                             double y,
                             double width) {
        textArea = new TextArea(text);
        getTextArea().setPrefWidth(width);
        getTextArea().setPrefHeight(width * 0.6182 * 0.6182);
        getTextArea().setEditable(false);
        anchor = createAnchor(x,y);
        getTextArea().translateXProperty().bind(anchor.centerXProperty());
        getTextArea().translateYProperty().bind(anchor.centerYProperty());
        getTextArea().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (onMouseDoubleClick != null && e.getClickCount() > 1) {
                onMouseDoubleClick.accept(e);
            }
        });
        /*textArea.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.isShiftDown()) {
                if (onMouseReleased != null) {
                    onMouseReleased.accept(e);
                }
            }
        });
        textArea.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.isShiftDown()) {
                textArea.setText("");
            }
        });
        textArea.setOnKeyReleased(e -> {
            System.out.println(textArea.getText());
            if ((e.getCode() == KeyCode.ENTER) && (e.isShiftDown() == true)) {
                System.out.println("SAVED!");
            }
        });*/
    }
    //region Events
    private Consumer<MouseEvent> onMouseDoubleClick;
    public void setOnMouseDoubleClick(Consumer<MouseEvent> onMouseDoubleClick) {
        this.onMouseDoubleClick =onMouseDoubleClick ;
    }
    //eventsregion Events

    //region Public Methods
    private Group _view;
    public Group view() {
        if (_view == null) {
            _view = new Group();
            _view.getChildren().addAll(textArea,anchor);
        }
        return _view;
    }
    //endregion Public Methods

    //region Properties
    public TextArea getTextArea() {
        return textArea;
    }
    //endregion Properties

    //region Private Methods
    private Circle createAnchor(double x, double y) {
        Circle a = new Circle();
        a.setFill(Color.TRANSPARENT);
        a.setStroke(Color.BLACK);
        a.setRadius(5);
        a.setCenterX(x);
        a.setCenterY(y);

        final ObjectProperty<Point2D> mousePressPoint = new SimpleObjectProperty<>();
        a.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            //onMousePressed();
            event.consume();
        });
        a.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            a.setVisible(false);
            double deltaX = event.getX()-mousePressPoint.get().getX();
            double deltaY = event.getY()-mousePressPoint.get().getY();
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            double oldCenterX = a.getCenterX() ;
            double oldCenterY = a.getCenterY();
            a.setCenterX(oldCenterX+deltaX);
            a.setCenterY(oldCenterY + deltaY);
            //onMouseDragged(event);
            event.consume();
        });
        a.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            a.setVisible(true);
            mousePressPoint.set(null) ;
            /*if (onMouseReleased != null) {
                onMouseReleased.apply(event, anchor);
            }*/
            event.consume();
        });
        return a;
    }




    //endregion Private Methods
}

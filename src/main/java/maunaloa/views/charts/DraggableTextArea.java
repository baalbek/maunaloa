package maunaloa.views.charts;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 14.05.14
 * Time: 11:04
 */
public class DraggableTextArea {
    private Circle anchor;
    private TextArea textArea;

    public DraggableTextArea(String text, double x, double y) {
        textArea = new TextArea(text);
        textArea.setPrefWidth(300);
        anchor = createAnchor(x,y);
        textArea.translateXProperty().bind(anchor.centerXProperty());
        textArea.translateYProperty().bind(anchor.centerYProperty());
    }

    //region Private Methods
    private Circle createAnchor(double x, double y) {
        Circle a = new Circle();
        a.setFill(Color.TRANSPARENT);
        a.setStroke(Color.BLACK);
        a.setRadius(5);
        a.setCenterX(x);
        a.setCenterY(y);

        final ObjectProperty<Point2D> mousePressPoint = new SimpleObjectProperty<>();
        a.addEventHandler(event -> {
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            //onMousePressed();
            event.consume();
        });
        return a;
    }

    //endregion Private Methods
}

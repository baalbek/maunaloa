package maunaloa.views.charts;

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
        /*textArea.setTranslateX(x);
        textArea.setTranslateY(y);*/
        anchor = new Circle();
        anchor.setFill(Color.TRANSPARENT);
        anchor.setStroke(Color.BLACK);
        anchor.setRadius(5);
        anchor.setCenterX(x);
        anchor.setCenterY(y);
        textArea.translateXProperty().bind(anchor.centerXProperty());
        textArea.translateYProperty().bind(anchor.centerYProperty());
    }
}

package maunaloa.views.charts;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import maunaloa.MaunaloaStatus;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;
import oahu.dto.Tuple;
import oahu.dto.Tuple2;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by rcs on 24.10.15.
 *
 */
public class DateLine implements ChartItem {
    private LocalDate currentDate;
    private IRuler dateRuler;
    private IBoundaryRuler boundaryRuler;
    private double valueLabelDeltaX = 8.0;
    private double valueLabelDeltaY = 8.0;
    //private DoubleProperty anchorRadius = new SimpleDoubleProperty(7);
    private Group group ;
    private Line line;
    private Circle anchor;
    private Text valueLabel;

    public DateLine(LocalDate date, IRuler dateRuler, IRuler vruler) {
        this.boundaryRuler = (IBoundaryRuler)vruler;
        this.dateRuler = dateRuler;
        this.currentDate = date;
    }

    @Override
    public Node view() {
        if (group == null) {
            createGroup();
        }
       return group;
    }

    private String valueLabelText () {
        return currentDate.toString();
    }
    private Circle createAnchor() {
        final Circle anchor = new Circle();
        anchor.setCenterX(line.getEndX());
        anchor.setCenterY(line.getEndY());

        line.startXProperty().bindBidirectional(anchor.centerXProperty());
        line.endXProperty().bindBidirectional(anchor.centerXProperty());
        line.endYProperty().bindBidirectional(anchor.centerYProperty());

        anchor.setRadius(7); //anchor.radiusProperty().bind(anchorRadius);
        anchor.setStrokeWidth(0.5);
        anchor.setFill(Color.TRANSPARENT);
        anchor.setStroke(Color.BLACK);

        final ObjectProperty<Point2D> mousePressPoint = new SimpleObjectProperty<>();

        anchor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            mousePressPoint.set(new Point2D(event.getX(), event.getY()));
            event.consume();
        });
        anchor.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (mousePressPoint.get() != null) {
                double deltaX = event.getX()-mousePressPoint.get().getX();
                double deltaY = event.getY()-mousePressPoint.get().getY();
                mousePressPoint.set(new Point2D(event.getX(), event.getY()));
                double oldCenterX = anchor.getCenterX() ;
                double oldCenterY = anchor.getCenterY();
                anchor.setCenterX(oldCenterX + deltaX);
                anchor.setCenterY(oldCenterY+deltaY);


                valueLabel.setX(anchor.getCenterX()+valueLabelDeltaX);
                valueLabel.setY(anchor.getCenterY()-valueLabelDeltaY);

                event.consume();
            }
        });
        anchor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            anchor.setCenterX(dateRuler.snapTo(anchor.getCenterX()));
            currentDate = (LocalDate)dateRuler.calcValue(anchor.getCenterX());
            valueLabel.setText(valueLabelText());
            mousePressPoint.set(null) ;
            event.consume();
        });
        return anchor;
    }
    private void createGroup() {
        double y0 = boundaryRuler.getUpperLeft().getY();
        double y1 = boundaryRuler.getLowerRight().getY() - 50;
        double xDate = dateRuler.calcPix(currentDate);
        line = new Line(xDate, y0, xDate, y1);

        valueLabel = new Text(xDate + valueLabelDeltaX, y1 - valueLabelDeltaY, valueLabelText());
        anchor = createAnchor();

        group = new Group();
        group.getChildren().addAll(line,anchor,valueLabel);
    }

    public void updateRulers(Tuple2<IRuler<LocalDate>,IRuler<Double>> rulers) {
        dateRuler = rulers.first();
        boundaryRuler = (IBoundaryRuler)rulers.second();
        double newX = dateRuler.calcPix(currentDate);
        anchor.setCenterX(dateRuler.snapTo(newX));
        valueLabel.setX(newX+valueLabelDeltaX);
    }
    @Override
    public Optional<Node> commentsView() {
        return null;
    }

    @Override
    public void setEntityStatus(int value) {

    }

    @Override
    public MaunaloaStatus getStatus() {
        return null;
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {

    }

    @Override
    public Optional<List<CommentEntity>> getComments() {
        return null;
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {

    }
}

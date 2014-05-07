package maunaloa.controllers.helpers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import maunaloa.entities.windowdressing.FibLineEntity;
import maunaloa.views.charts.ChartItem;
import oahu.financial.Stock;
import oahux.chart.IRuler;
import oahux.controllers.MaunaloaChartViewModel;

import java.util.List;

/**
 * Created by rcs on 5/1/14.
 */
public class FibonacciHelper extends AbstractControllerHelper {

    public FibonacciHelper(MaunaloaChartViewModel boss) {
        super(boss);
    }

    //region Events
    ObjectProperty<Line> lineA = new SimpleObjectProperty<>();
    public void onNewFibonacciLine() {
        Stock stock = boss.getStock();
        if (stock == null) {
            return;
        }
        Pane myPane = boss.getPane();
        IRuler hruler = boss.getHruler();
        myPane.setOnMousePressed(e -> {
            double x = hruler.snapTo(e.getX());
            double y = e.getY();
            Line line = new Line(x, y, x, y);
            myPane.getChildren().add(line);
            lineA.set(line);
        });
        myPane.setOnMouseDragged(e -> {
            Line line = lineA.get();
            if (line != null) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
            }
        });
        myPane.setOnMouseReleased(e -> {
            Line line = lineA.get();
            if (line != null) {
                myPane.getChildren().remove(line);

                line.setStartX(hruler.snapTo(line.getStartX()));
                line.setEndX(hruler.snapTo(line.getEndX()));

                FibLineEntity fibline = new FibLineEntity(stock.getTicker(), boss.getLocation(), line, boss.getRulers());

                //myPane.getChildren().add(fibline.view());
                updateMyPaneLines(fibline, lineMap());
            }
            lineA.set(null);
            myPane.setOnMousePressed(null);
            myPane.setOnMouseDragged(null);
            myPane.setOnMouseReleased(null);
        });
    }
    //endregion Events

}

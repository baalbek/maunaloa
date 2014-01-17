package maunaloa.controllers.groovy

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import maunaloa.controllers.ChartCanvasController
import maunaloa.events.FibonacciEvent
import maunaloa.views.CanvasGroup
import maunaloa.views.FibonacciDraggableLine
import oahu.financial.Stock
import oahux.chart.IRuler

/**
 * Created by rcs on 1/15/14.
 */
class FibonacciController {

    ObjectProperty<Line> lineA = new SimpleObjectProperty<>()

    ChartCanvasController parent

    void notify(FibonacciEvent event) {

         if (event.getLocation() != this.location) return
         switch  (event.getAction()) {
             case FibonacciEvent.NEW_LINE:
                 activateFibonacci()
                 break
             case FibonacciEvent.DELETE_SEL_LINES:
                 //deleteLines(fibLines,false)
                 break
             case FibonacciEvent.DELETE_ALL_LINES:
                 //deleteLines(fibLines,true)
                 break
         }
    }

    private void activateFibonacci() {
        Pane myPane = parent.getMyPane()
        myPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                double x = parent.getHruler().snapTo(evt.getX())
                double y = evt.getY()
                Line line = new Line(x, y, x, y)
                myPane.getChildren().add(line)
                lineA.set(line)
            }
        })
        myPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                Line line = lineA.get()
                if (line != null) {
                    line.setEndX(evt.getX())
                    line.setEndY(evt.getY())
                }
            }
        })
        myPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                Line line = lineA.get()
                if (line != null) {
                    myPane.getChildren().remove(line)
/*                    if (log.isDebugEnabled()) {
                        log.debug(String.format("Has Fibonacci extension: %s", fibonacci1272extProperty().get()))
                    }*/
                    line.setStartX(getHruler().snapTo(line.getStartX()))
                    line.setEndX(getHruler().snapTo(line.getEndX()))
                    final CanvasGroup fibLine = new FibonacciDraggableLine(line,
                            parent.getHruler(),
                            parent.getVruler(),
                            fibonacci1272extProperty().get())
                    updateMyPaneLines(fibLine,fibLines)
                }
                lineA.set(null)
                myPane.setOnMousePressed(null)
                myPane.setOnMouseDragged(null)
                myPane.setOnMouseReleased(null)
            }
        })
    }

    private void updateMyPaneLines(CanvasGroup line, Map<Stock,List<CanvasGroup>> linesMap) {

    }

    private Map<Stock,List<CanvasGroup>> fibLines = new HashMap<>()
}

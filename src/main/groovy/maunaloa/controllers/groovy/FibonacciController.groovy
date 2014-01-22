package maunaloa.controllers.groovy

import com.mongodb.DBObject
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import maunaloa.controllers.ChartCanvasController
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent
import maunaloa.views.CanvasGroup
import maunaloa.views.FibonacciDraggableLine
import maunaloa.views.MongodbLine
import oahu.financial.Stock
import oahux.chart.IRuler
import org.apache.log4j.Logger
import org.bson.types.ObjectId

/**
 * Created by rcs on 1/15/14.
 */
class FibonacciController extends ChartCanvasControllerHelper {

    ObjectProperty<Line> lineA = new SimpleObjectProperty<>()

    //ChartCanvasController parent

    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {

    }

    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {
        IRuler vruler = parent.getVruler()
        IRuler hruler = parent.getHruler()
        def createLineFromDBObject = { DBObject obj ->
            DBObject p1 = (DBObject)obj.get("p1")
            DBObject p2 = (DBObject)obj.get("p2")

            double p1x = hruler.calcPix(p1.get("x"))
            double p1y = vruler.calcPix(p1.get("y"))

            double p2x = hruler.calcPix(p2.get("x"))
            double p2y = vruler.calcPix(p2.get("y"))

            Line line = new Line()
            line.setStartX(p1x)
            line.setStartY(p1y)
            line.setEndX(p2x)
            line.setEndY(p2y)
            return line
        }

        for (DBObject o : event.getFibonacci()) {
            Line line = createLineFromDBObject(o)
            MongodbLine fibLine = new FibonacciDraggableLine(line,
                    hruler,
                    vruler)

            fibLine.setMongodbId((ObjectId)o.get("_id"))

            updateMyPaneLines((CanvasGroup) fibLine, fibLines)
            println o
        }
    }

    void activateFibonacci() {
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
                    line.setStartX(parent.getHruler().snapTo(line.getStartX()))
                    line.setEndX(parent.getHruler().snapTo(line.getEndX()))
                    final CanvasGroup fibLine = new FibonacciDraggableLine(line,
                            parent.getHruler(),
                            parent.getVruler())
                    updateMyPaneLines(fibLine,fibLines)
                }
                lineA.set(null)
                myPane.setOnMousePressed(null)
                myPane.setOnMouseDragged(null)
                myPane.setOnMouseReleased(null)
            }
        })
    }

    private Map<Stock,List<CanvasGroup>> fibLines = new HashMap<>()
}

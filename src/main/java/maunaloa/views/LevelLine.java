package maunaloa.views;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import oahu.financial.StockPrice;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import oahux.domain.DerivativeFx;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/10/13
 * Time: 3:11 PM
 */
public class LevelLine implements CanvasLine {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private Line line ;
    private Group group ;
    private final IBoundaryRuler ruler;
    private final DerivativeFx derivative;

    public LevelLine(DerivativeFx derivative, IRuler ruler) {
        this.derivative = derivative;
        this.ruler = (IBoundaryRuler)ruler;
        group = new Group();

        Point2D pt0 = ((IBoundaryRuler)ruler).getUpperLeft();
        Point2D pt = ((IBoundaryRuler)ruler).getLowerRight();

        double yRisc = ruler.calcPix(derivative.stockPriceRiskProperty().get());
        Line risc = new Line(pt0.getX(),yRisc,pt.getX(),yRisc);
        risc.setStroke(Color.RED);
        group.getChildren().add(risc);


        double yBe = ruler.calcPix(derivative.getBreakeven());
        Line breakEven = new Line(pt0.getX(),yBe,pt.getX(),yBe);
        breakEven.setStroke(Color.GREEN);
        group.getChildren().add(breakEven);

        /*
        double sprice = ruler.calcPix(derivative.getParent().getCls());
        Line sp = new Line(pt0.getX(),sprice,pt.getX(),sprice);
        group.getChildren().add(sp);
        */

        if (log.isDebugEnabled()) {
            //log.info(String.format("LevelLine for StockPrice %s: y %.2f, value: %.2f", p.getStock().getTicker(), y, p.getCls()));
            log.info(String.format("LevelLine for derivative %s", derivative.getTicker()));
        }
    }


    @Override
    public Node view() {
        return group;
    }
}

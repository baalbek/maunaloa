package maunaloa.views;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
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
public class RiscLines implements CanvasGroup {
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private Line line ;
    private Group group ;
    private final IBoundaryRuler ruler;
    private final DerivativeFx derivative;

    public RiscLines(DerivativeFx derivative, IRuler ruler) {
        this.derivative = derivative;
        this.ruler = (IBoundaryRuler)ruler;
        group = new Group();

        createLevel(derivative.getBreakeven(), Color.GREEN,
                String.format("(%s) Break-even: %.2f",
                        derivative.getTicker(),
                        derivative.getBreakeven()));
        createLevel(derivative.getBreakeven(), Color.RED,
                String.format("(%s) Risc: %.2f",
                        derivative.getTicker(),
                        derivative.stockPriceRiskProperty().get()));

        /*
        double sprice = ruler.calcPix(derivative.getParent().getCls());
        Line sp = new Line(pt0.getX(),sprice,pt.getX(),sprice);
        group.getChildren().add(sp);
        */

        if (log.isDebugEnabled()) {
            //log.info(String.format("RiscLines for StockPrice %s: y %.2f, value: %.2f", p.getStock().getTicker(), y, p.getCls()));
            log.info(String.format("RiscLines for derivative %s", derivative.getTicker()));
        }
    }

    private void createLevel(double value, Color lineColor, String text){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(value);
        Line line = new Line(pt0.getX(),yBe,pt.getX(),yBe);
        line.setStroke(lineColor);
        group.getChildren().add(line);
        group.getChildren().add(new Text(pt0.getX()+20,yBe-8,text));
    }


    @Override
    public Node view() {
        return group;
    }
}

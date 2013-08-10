package maunaloa.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import oahu.exceptions.NotImplementedException;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import oahux.domain.DerivativeFx;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/10/13
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class LevelLine implements CanvasLine {
    private Line line ;
    private Group group ;
    private final IBoundaryRuler ruler;
    private final DerivativeFx derivative;

    public LevelLine(DerivativeFx derivative, IRuler ruler) {
        this.derivative = derivative;
        this.ruler = (IBoundaryRuler)ruler;
    }


    @Override
    public Node view() {
        return group;
    }
}

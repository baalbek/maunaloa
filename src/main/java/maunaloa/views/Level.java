package maunaloa.views;

import com.mongodb.BasicDBObject;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import oahu.exceptions.NotImplementedException;
import oahux.chart.IBoundaryRuler;
import oahux.chart.IRuler;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 20.11.13
 * Time: 09:40
 */
public class Level implements CanvasGroup, MongodbLine {
    //region Init
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
    private Group group ;

    private final IBoundaryRuler ruler;
    private final double levelValue;
    private final Color lineColor;

    public Level(double levelValue, IRuler ruler) {
        this(levelValue, ruler, Color.BLACK);
    }

    public Level(double levelValue, IRuler ruler, Color lineColor) {
        this.ruler = (IBoundaryRuler)ruler;
        this.levelValue = levelValue;
        this.lineColor = lineColor;
    }

    public static Level createFromPix(double pix, IRuler ruler, Color lineColor) {
        return new Level(pix, ruler, lineColor);
    }
    //endregion Init

    //region Private Methods
    private void createLevel(double value, Color lineColor, String text){
        Point2D pt0 = ruler.getUpperLeft();
        Point2D pt = ruler.getLowerRight();
        double yBe = ruler.calcPix(value);
        Line line = new Line(pt0.getX(),yBe,pt.getX(),yBe);
        line.setStroke(lineColor);
        group.getChildren().add(line);
        group.getChildren().add(new Text(pt0.getX()+20,yBe-8,text));
    }
    //endregion Private Methods

    //region interface CanvasGroup
    @Override
    public Node view() {
        if (group == null) {
            createLevel(levelValue, lineColor, String.format("%.f", levelValue));
        }
        return group;
    }

    @Override
    public void setStatus(int status) {
        throw new NotImplementedException();
    }

    @Override
    public int getStatus() {
        throw new NotImplementedException();
    }
    //endregion interface CanvasGroup

    //region interface MongodbLine
    @Override
    public ObjectId getMongodbId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMongodbId(ObjectId id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getActive() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setActive(boolean value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BasicDBObject coord(int pt) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getLocation() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addComment(String comment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getComments() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    //endregion interface MongodbLine
}

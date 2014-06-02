package maunaloa.views.charts;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import maunaloa.MaunaloaStatus;
import maunaloa.StatusCodes;
import maunaloa.entities.windowdressing.CommentEntity;
import maunaloa.repository.WindowDressingRepository;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by rcs on 5/27/14.
 */
public class SpotItem implements ChartItem {
    private Group group ;
    private MaunaloaStatus maunaloaStatus;
    private final StockPrice spot;
    private final IRuler hruler,vruler;
    private final LocalDate dx;
    public SpotItem(StockPrice spot, IRuler hruler, IRuler vruler, LocalDate dx) {
        this.spot = spot;
        this.hruler = hruler;
        this.vruler = vruler;
        this.dx = dx;
    }
    public SpotItem(StockPrice spot, IRuler hruler, IRuler vruler) {
        this(spot,hruler,vruler,LocalDate.now());
    }
    @Override
    public Node view() {
        if (group == null) {
            group = new Group();
            Instant instant = dx.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date legacyDx = Date.from(instant);
            double x = hruler.calcPix(legacyDx);
            double yHi = vruler.calcPix(spot.getHi());
            double yLo = vruler.calcPix(spot.getLo());

            double opn = spot.getOpn();
            double cls = spot.getCls();

            Rectangle r = null;
            if (cls > opn) { // Green candlestick
                r = new Rectangle(x, cls, 5, cls-opn);
                r.setFill(Color.GREEN);
            }
            else {
                r = new Rectangle(x, opn, 5, opn-cls);
                r.setFill(Color.RED);
            }

           Line wicks = new Line(x, yHi, x, yLo);
            group.getChildren().addAll(wicks, r);
        }
        return group;
    }

    @Override
    public Optional<Node> commentsView() {
        return Optional.empty();
    }

    @Override
    public MaunaloaStatus getStatus() {
        if (maunaloaStatus == null) {
            IntegerProperty entStatus = new SimpleIntegerProperty(StatusCodes.NA);
            IntegerProperty lineStatus = new SimpleIntegerProperty(StatusCodes.NA);
            if (maunaloaStatus == null) {
                return new MaunaloaStatus(entStatus,lineStatus);
            }
        }
        return maunaloaStatus;
    }

    @Override
    public void saveToRepos(WindowDressingRepository repos) {
    }

    @Override
    public Optional<List<CommentEntity>> getComments() {
        return Optional.empty();
    }

    @Override
    public void removeFrom(ObservableList<Node> container) {
        if (group != null) {
            container.remove(group);
        }
    }
}

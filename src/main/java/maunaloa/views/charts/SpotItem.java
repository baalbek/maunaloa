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
import maunaloa.service.Logx;
import oahu.exceptions.NotImplementedException;
import oahu.financial.StockPrice;
import oahux.chart.IRuler;
import org.apache.log4j.Logger;

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
    private Logger log = Logger.getLogger(getClass().getPackage().getName());
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

            double yOpn = vruler.calcPix(spot.getOpn());
            double yCls = vruler.calcPix(spot.getCls());

            Line wix = new Line(x, yHi, x, yLo);
            double wixW = 8;
            double xBody = x - 4;

            if (Math.abs(cls-opn) < 0.1) { // Doji
                Line dojiLine = new Line(x-5,yOpn,x+5,yOpn);
                Logx.debug(log,
                        () -> String.format("DOJI, yOpn: %.2f, yHi: %.2f, yLo: %.2f, yCls: %.2f",
                                yOpn, yHi, yLo, yCls));
                group.getChildren().addAll(wix, dojiLine);
            }
            else if (cls > opn) { // Green candlestick
                Rectangle r = new Rectangle(xBody, yCls, wixW, yOpn-yCls);
                r.setFill(Color.GREEN);
                Logx.debug(log,
                        () -> String.format("GREEN candlestick, yOpn: %.2f, yHi: %.2f, yLo: %.2f, yCls: %.2f",
                                yOpn, yHi, yLo, yCls));
                group.getChildren().addAll(wix, r);
            }
            else {
                Rectangle r = new Rectangle(xBody, yOpn, wixW, yCls-yOpn);
                r.setFill(Color.RED);
                Logx.debug(log,
                        () -> String.format("RED candlestick, yOpn: %.2f, yHi: %.2f, yLo: %.2f, yCls: %.2f",
                                yOpn, yHi, yLo, yCls));
                group.getChildren().addAll(wix, r);
            }

        }
        return group;
    }

    @Override
    public Optional<Node> commentsView() {
        return Optional.empty();
    }

    @Override
    public void setEntityStatus(int value) {
        throw new NotImplementedException();
    }

    @Override
    public MaunaloaStatus getStatus() {
        if (maunaloaStatus == null) {
            IntegerProperty entStatus = new SimpleIntegerProperty(StatusCodes.NA);
            //IntegerProperty lineStatus = new SimpleIntegerProperty(StatusCodes.NA);
            if (maunaloaStatus == null) {
                return new MaunaloaStatus(entStatus,null,null);
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

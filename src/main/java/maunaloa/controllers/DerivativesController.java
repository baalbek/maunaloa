package maunaloa.controllers;


import javafx.collections.ObservableList;
import oahu.financial.beans.DerivativeBean;
import oahu.views.MaunaloaChart;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.11.12
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public interface DerivativesController {
    ObservableList<DerivativeBean> derivatives();
    void setChart(MaunaloaChart chart);
    void draw();
    void setTicker(String ticker);
}

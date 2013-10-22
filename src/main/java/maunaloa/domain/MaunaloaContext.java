package maunaloa.domain;

import maunaloa.events.MainFrameControllerListener;
import maunaloa.models.MaunaloaFacade;
import oahu.financial.Stock;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 15.10.13
 * Time: 12:57
 */
public class MaunaloaContext {
    private Stock stock;
    private MaunaloaFacade facade;
    private List<MainFrameControllerListener> listeners;
    private MainFrameControllerListener listener;
    private int location;
    private Date startDate;
    private Date endDate;


    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public MaunaloaFacade getFacade() {
        return facade;
    }

    public void setFacade(MaunaloaFacade facade) {
        this.facade = facade;
    }

    public List<MainFrameControllerListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<MainFrameControllerListener> listeners) {
        this.listeners = listeners;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public MainFrameControllerListener getListener() {
        return listener;
    }

    public void setListener(MainFrameControllerListener listener) {
        this.listener = listener;
    }
}

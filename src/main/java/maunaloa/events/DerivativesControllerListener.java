package maunaloa.events;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/10/13
 * Time: 1:24 PM
 */
public interface DerivativesControllerListener {
    void notify(DerivativesCalculatedEvent event);
    void notify(StockPriceAssignedEvent event);
}

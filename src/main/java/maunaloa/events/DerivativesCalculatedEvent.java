package maunaloa.events;

import oahu.financial.Derivative;
import oahux.domain.DerivativeFx;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 8/10/13
 * Time: 1:24 PM
 */
public class DerivativesCalculatedEvent {
    private final List<DerivativeFx> calculated;

    public DerivativesCalculatedEvent(List<DerivativeFx> calculated) {
        this.calculated = calculated;
    }
    public List<DerivativeFx> getCalculated()  {
        return calculated;
    }
}

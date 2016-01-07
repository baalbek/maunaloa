package maunaloa.derivatives;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 9/15/13
 * Time: 12:29 PM
 */
public class RiscItem {
    private final double value;
    public RiscItem(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%.1f",getValue());
    }
}

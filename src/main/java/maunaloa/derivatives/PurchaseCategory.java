package maunaloa.derivatives;

/**
 * Created by rcs on 25.08.14.
 */
public class PurchaseCategory {
    private final int value;
    private final String desc;

    public PurchaseCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    @Override
    public String toString() {
        return String.format("[ %d ] %s", getValue(), getDesc());
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}

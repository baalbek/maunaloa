package maunaloa.domain;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 11/14/13
 * Time: 9:32 PM
 */
public class TreeViewItemWrapper {
    private final Object wrapped;

    public TreeViewItemWrapper(Object wrapped) {
        this.wrapped = wrapped;
    }
    @Override
    public String toString() {
        return wrapped.toString();
    }

    public static TreeViewItemWrapper createDefault(String desc) {
        return new TreeViewItemWrapper(desc);
    }

    public Object getWrapped() {
        return wrapped;
    }
}

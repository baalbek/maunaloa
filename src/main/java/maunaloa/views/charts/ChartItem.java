package maunaloa.views.charts;

import javafx.scene.Node;
import maunaloa.MaunaloaStatus;
import maunaloa.repository.WindowDressingRepository;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 24.04.14
 * Time: 15:14
 */
public interface ChartItem {
    public static int NA = 0;
    public static int SELECTED = 1;
    public static int DIRTY = 2;
    Node view();
/*    int getStatus();
    void setStatus(int value);*/
    MaunaloaStatus getStatus();
    void saveToRepos(WindowDressingRepository repos);
}

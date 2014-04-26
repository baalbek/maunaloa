package maunaloa.mocks;

import maunaloa.controllers.ControllerHub;
import maunaloa.repository.WindowDressingRepository;
import oahux.controllers.MaunaloaChartViewModel;

/**
 * Created by rcs on 4/26/14.
 */
public class MockHub implements ControllerHub {
    private WindowDressingRepository windowDressingRepository;

    @Override
    public MaunaloaChartViewModel getViewModel(int location) {
        return new MockViewModel();
    }
    public void setWindowDressingRepository(WindowDressingRepository windowDressingRepository) {
        this.windowDressingRepository = windowDressingRepository;
        this.windowDressingRepository.setControllerHub(this);
    }
}

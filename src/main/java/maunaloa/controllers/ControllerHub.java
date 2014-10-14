package maunaloa.controllers;

import maunaloa.repository.WindowDressingRepository;
import oahu.financial.repository.StockMarketRepository;

/**
 * Created by rcs on 4/24/14.
 */
public interface ControllerHub {
    WindowDressingRepository getWindowDressingRepository();
    StockMarketRepository getStockRepository();
}

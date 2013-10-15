package maunaloa.models;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 03.10.13
 * Time: 09:39
 */

import javafx.scene.Node;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahux.domain.DerivativeFx;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.11.12
 * Time: 14:59
 */
public interface MaunaloaFacade {
    Collection<StockPrice> stockPrices(String ticker, Date fromDx, int period);
    Collection<StockPrice> stockPrices(String ticker, int period);
    StockPrice spot(String ticker);
    Collection<DerivativeFx> calls(String ticker);
    Collection<DerivativeFx> puts(String ticker);
    Collection<DerivativeFx> callsAndPuts(String ticker);
    Collection<Node> fibLines(String ticker);
    void addFibLine(String ticker, Node line);
    List<Stock> getTickers();
    ChartWindowDressingModel getWindowDressingModel();
}

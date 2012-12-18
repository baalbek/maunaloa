package maunaloa.models.mybatis;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import oahu.financial.beans.StockBean;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 22.11.12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */



/**
 *
 * @author rcs
 */
public interface StockMapper {
    List<StockBean> selectTickerAll(String ticker);

    List<StockBean> selectTicker2(@Param("ticker") String ticker,
                              @Param("fromDx") Date fromDx,
                              @Param("toDx") Date toDx);

    List<StockBean> selectTicker(@Param("ticker") String ticker,
                             @Param("fromDx") Date fromDx);

    List<StockBean> selectTickerWeek(@Param("ticker") String ticker,
                                 @Param("fromDx") Date fromDx);

    //void insertStockPrice(maunakea.financial.impl.StockImpl item);
}

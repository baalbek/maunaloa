package maunaloa.models.mybatis;

import oahu.financial.beans.StockBean;
import oahu.financial.beans.StockTickerBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 4/18/13
 * Time: 8:52 PM
 */

public interface StockMapper {
    List<StockBean> selectStocks(@Param("tickerId") int tickerId,
                                 @Param("fromDx") Date fromDx);

    List<StockTickerBean> selectTickers();

}

package com.antworking.core.filter;

import com.antworking.core.order.Order;
import com.antworking.model.base.BaseCollectModel;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public interface IFilter extends Order {

    default int order(){
        return 0;
    }

    boolean filter(BaseCollectModel model);
}

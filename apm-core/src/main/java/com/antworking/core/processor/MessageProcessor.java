package com.antworking.core.processor;

import com.antworking.core.order.Order;

public interface MessageProcessor extends Order {

    default int order(){
        return 0;
    }




}

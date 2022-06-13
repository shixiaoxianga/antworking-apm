package com.antworking.core.order;

public interface Order {

    default int order(){
        return 0;
    }
}

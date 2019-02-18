package com.d.pay.core;

import com.d.order.entity.OrderInfo;

public interface Pay {
    void pay(OrderInfo orderInfo);
}

package com.d.pay.service;

import com.d.pay.core.PayChannel;
import com.d.pay.core.Terminal;

public interface PayService {
    void pay(PayChannel channel, Terminal terminalEnum, Long orderId);
}

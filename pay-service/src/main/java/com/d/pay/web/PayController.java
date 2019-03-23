package com.d.pay.web;

import com.d.pay.core.PayChannel;
import com.d.pay.core.Terminal;
import com.d.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

public class PayController {
    @Autowired
    private PayService payService;

    @RequestMapping(path = "/pay")
    public void pay(PayChannel channel, Terminal terminal, Long orderId) {
        payService.pay(channel, terminal, orderId);
    }
}

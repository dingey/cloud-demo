package com.d.pay.impl;

import com.d.client.order.OrderClient;
import com.d.order.entity.OrderInfo;
import com.d.pay.core.*;
import com.d.pay.service.PayService;
import com.d.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
    private final AlipayService alipayService;
    private final BalancePayService balancePayService;
    private final OrderClient orderClient;
    private final WeixinPayService weixinPayService;

    @Autowired
    public PayServiceImpl(AlipayService alipayService, BalancePayService balancePayService, OrderClient orderClient, WeixinPayService weixinPayService) {
        this.alipayService = alipayService;
        this.balancePayService = balancePayService;
        this.orderClient = orderClient;
        this.weixinPayService = weixinPayService;
    }

    @Override
    public void pay(PayChannel channel, Terminal terminalEnum, Long orderId) {
        OrderInfo orderInfo = orderClient.get(orderId);
        Assert.notNull(orderInfo, "订单不存在");
        Assert.verifyLogin();
        if (PayChannel.ALIPAY == channel) {
            alipayService.pay(orderInfo);
        } else if (PayChannel.WXPAY == channel) {
            weixinPayService.pay(orderInfo);
        } else {
            balancePayService.pay(orderInfo);
        }
    }
}

package com.d.pay.core;

import com.d.order.entity.OrderInfo;
import com.d.util.Assert;
import com.d.util.ServletUtil;

public class BalancePayServiceImpl extends AbstractPayImpl implements BalancePayService {
    @Override
    public void pay(OrderInfo orderInfo) {
        String password = ServletUtil.getRequest().getParameter("password");
        Assert.notNull(password, "请输入支付密码");
        //TODO
    }
}

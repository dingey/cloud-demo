package com.d.pay.core;

import com.d.client.user.UserClient;
import com.d.order.entity.OrderInfo;
import com.d.pay.config.PayConfig;
import com.d.user.entity.User;
import com.d.util.Assert;
import com.d.util.JsonUtil;
import com.d.util.ServletUtil;
import com.github.dingey.weixin.SignType;
import com.github.dingey.weixin.WeixinSDK;
import com.github.dingey.weixin.model.Unifiedorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.Map;

@Component
public class WeixinPayServiceImpl extends AbstractPayImpl implements WeixinPayService {
    @Autowired
    private PayConfig payConfig;
    @Autowired
    private UserClient userClient;
    @Autowired
    private WeixinSDK sdk;

    @Override
    public void pay(OrderInfo orderInfo) {
        try {
            User user = userClient.get(orderInfo.getUserId());
            Assert.notNull(user, "用户不存在");
            Unifiedorder order = new Unifiedorder();
            order.setAppId(payConfig.getAppid());
            order.setMchId(payConfig.getMchId());
            order.setTotalFee(orderInfo.getPayAmount().toString());
            order.setOutTradeNo(orderInfo.getId().toString());
            order.setSpbillCreateIp(ServletUtil.getRequest().getRemoteAddr());
            order.setOpenId(user.getOpenid());
            order.setNotifyUrl(payConfig.getHost() + "/pay/notify");
            Map<String, String> map = sdk.unifiedorder(order);
            if ("SUCCESS".equals(map.get("result_code"))) {
                PrintWriter writer = ServletUtil.getResponse().getWriter();
                writer.write(JsonUtil.toJsonString(map));
                writer.flush();
                writer.close();
            } else {
                throw new RuntimeException(map.get("err_code_des"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

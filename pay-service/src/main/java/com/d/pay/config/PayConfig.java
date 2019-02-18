package com.d.pay.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PayConfig {
    @Value("${pay.wxpay.appid:123456}")
    private String appid;
    @Value("${pay.weixin.mchid:61234646}")
    private String mchId;
    @Value("${pay.host:127.0.0.1}")
    private String host;
}

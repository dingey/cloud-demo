package com.d.pay.config;

import com.github.dingey.weixin.WeixinSDK;
import com.github.dingey.weixin.WeixinSDKBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeixinSdkConfig {
    @Bean
    public WeixinSDK build() {
        return WeixinSDKBuilder.build();
    }
}

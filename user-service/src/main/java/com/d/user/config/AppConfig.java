package com.d.user.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.d.base.Const;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(Const.PROD)
@Configuration
@EnableApolloConfig
public class AppConfig {
}

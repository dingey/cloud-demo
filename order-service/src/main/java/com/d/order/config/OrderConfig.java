package com.d.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class OrderConfig {
	/**
	 * 订单是否异步提交
	 */
	@Value("${order.async.submit:false}")
	private boolean orderAsyncSubmit;
}

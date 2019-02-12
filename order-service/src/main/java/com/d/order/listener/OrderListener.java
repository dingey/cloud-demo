package com.d.order.listener;

import com.d.base.Const;
import com.d.order.dto.OrderDTO;
import com.d.order.service.OrderInfoService;
import com.d.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单创建监听器
 */
@Slf4j
@Component
@RabbitListener(queues = Const.TOPIC_ORDER_CREATE)
public class OrderListener {
	private final OrderInfoService orderInfoService;

	@Autowired
	public OrderListener(OrderInfoService orderInfoService) {
		this.orderInfoService = orderInfoService;
	}

	@RabbitHandler
	public void process(String message) {
		if (message == null || message.isEmpty()) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("【订单创建监听器】新订单事件【{}】", message);
		}
		OrderDTO dto = JsonUtil.single().fromJson(message, OrderDTO.class);
		orderInfoService.submit(dto);
	}
}

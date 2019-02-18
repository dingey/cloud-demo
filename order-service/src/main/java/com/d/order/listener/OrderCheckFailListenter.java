package com.d.order.listener;

import com.d.base.Const;
import com.d.enums.OrderStatus;
import com.d.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = Const.TOPIC_ORDER_CHECK_FAIL)
public class OrderCheckFailListenter {
    private final OrderInfoService orderInfoService;
    private final StringRedisTemplate srt;

    @Autowired
    public OrderCheckFailListenter(OrderInfoService orderInfoService, StringRedisTemplate srt) {
        this.orderInfoService = orderInfoService;
        this.srt = srt;
    }

    @RabbitHandler
    public void process(String message) {
        Long orderId = Long.valueOf(message);
        orderInfoService.storageShortage(Long.valueOf(message));
        srt.opsForHash().put(Const.CACHE_KEY_ORDER + orderId, "status", OrderStatus.INVENTORY_SHORTAGE.getCode());
        srt.opsForZSet().add(Const.CACHE_KEY_ORDER_EXPIRE, message, System.currentTimeMillis() / 1000 + Const.ORDER_EXPIRE_TIME);
    }
}

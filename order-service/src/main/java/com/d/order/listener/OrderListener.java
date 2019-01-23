package com.d.order.listener;

import com.d.base.Const;
import com.d.client.goods.GoodsClient;
import com.d.order.dto.OrderDTO;
import com.d.order.entity.OrderInfo;
import com.d.order.service.OrderInfoService;
import com.d.order.service.OrderItemService;
import com.d.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther d
 */
@Slf4j
@Component
@RabbitListener(queues = Const.TOPIC_ORDER_CREATE)
public class OrderListener {
    private final OrderInfoService orderInfoService;
    private final OrderItemService orderItemService;
    private final GoodsClient goodsClient;

    @Autowired
    public OrderListener(OrderInfoService orderInfoService, OrderItemService orderItemService, GoodsClient goodsClient) {
        this.orderInfoService = orderInfoService;
        this.orderItemService = orderItemService;
        this.goodsClient = goodsClient;
    }

    @RabbitHandler
    public void process(String message) {
        OrderDTO dto = JsonUtil.single().fromJson(message, OrderDTO.class);
        if (dto == null || dto.getItem() == null) {
            return;
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setNewRecord(true);
        orderInfo.setId(dto.getOrderId());
        orderInfo.setUserId(dto.getUserId());
        orderInfoService.save(orderInfo);
        for (OrderDTO.OrderItemDTO itemDTO : dto.getItem()) {

        }
    }
}

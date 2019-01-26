package com.d.order.listener;

import com.d.base.Const;
import com.d.client.goods.GoodsClient;
import com.d.client.goods.GoodsSkuClient;
import com.d.common.service.IdService;
import com.d.goods.entity.Goods;
import com.d.goods.entity.GoodsSku;
import com.d.order.dto.OrderDTO;
import com.d.order.entity.OrderInfo;
import com.d.order.entity.OrderItem;
import com.d.order.service.OrderInfoService;
import com.d.order.service.OrderItemService;
import com.d.util.Assert;
import com.d.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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
    private final IdService idService;
    private final OrderInfoService orderInfoService;
    private final OrderItemService orderItemService;
    private final GoodsClient goodsClient;
    private final GoodsSkuClient goodsSkuClient;
    private final AmqpTemplate rabbit;

    @Autowired
    public OrderListener(IdService idService, OrderInfoService orderInfoService, OrderItemService orderItemService, GoodsClient goodsClient, GoodsSkuClient goodsSkuClient, AmqpTemplate rabbit) {
        this.idService = idService;
        this.orderInfoService = orderInfoService;
        this.orderItemService = orderItemService;
        this.goodsClient = goodsClient;
        this.goodsSkuClient = goodsSkuClient;
        this.rabbit = rabbit;
    }

    @RabbitHandler
    public void process(String message) {
        OrderDTO dto = JsonUtil.single().fromJson(message, OrderDTO.class);
        if (dto == null || dto.getItem() == null) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("【订单创建监听器】,订单内容：{}", message);
        }
        Integer totalAmount = 0;
        Integer postageAmount = 0;
        Integer discountAmount = 0;
        for (OrderDTO.OrderItemDTO itemDTO : dto.getItem()) {
            Goods goods = goodsClient.getBySkuId(itemDTO.getSkuId());
            GoodsSku sku = goodsSkuClient.get(itemDTO.getSkuId());
            Assert.buyCheck(goods);
            //TODO 优惠券校验
            OrderItem orderItem = new OrderItem();
            orderItem.setId(idService.nextId());
            orderItem.setOrderId(dto.getOrderId());
            orderItem.setQty(itemDTO.getQty());
            orderItem.setGoodsSkuId(itemDTO.getSkuId());
            orderItem.setNewRecord(true);
            orderItem.setUserId(dto.getUserId());
            orderItem.setUnitPrice(sku.getPrice());
            orderItem.setQty(sku.getQty());
            orderItem.setTotalAmount(sku.getPrice() * itemDTO.getQty());
            orderItem.setCouponId(itemDTO.getCouponId());
            orderItem.setDiscountAmount(0);
            orderItemService.save(orderItem);
            totalAmount += orderItem.getTotalAmount();
            discountAmount += orderItem.getDiscountAmount();
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setNewRecord(true);
        orderInfo.setId(dto.getOrderId());
        orderInfo.setUserId(dto.getUserId());
        orderInfo.setDiscountAmount(discountAmount);
        orderInfo.setPostageAmount(postageAmount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfoService.save(orderInfo);
        rabbit.convertAndSend(Const.TOPIC_ORDER_CHECK, message);
    }
}

package com.d.order.impl;

import com.d.order.service.OrderInfoService;
import com.d.order.service.OrderItemService;
import com.d.util.Assert;
import com.di.kit.JsonUtil;

import lombok.extern.slf4j.Slf4j;

import com.d.base.BaseServiceImpl;
import com.d.base.Const;
import com.d.client.goods.GoodsClient;
import com.d.client.goods.GoodsSkuClient;
import com.d.common.service.IdService;
import com.d.enums.OrderStatus;
import com.d.goods.entity.Goods;
import com.d.goods.entity.GoodsSku;
import com.d.order.mapper.OrderInfoMapper;
import com.d.order.dto.OrderDTO;
import com.d.order.entity.OrderInfo;
import com.d.order.entity.OrderItem;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 订单信息service
 *
 * @author d
 */
@Slf4j
@Service
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    private final IdService idService;
    private final OrderItemService orderItemService;
    private final GoodsClient goodsClient;
    private final GoodsSkuClient goodsSkuClient;
    private final AmqpTemplate rabbit;
    private final StringRedisTemplate srt;

    public OrderInfoServiceImpl(IdService idService, OrderItemService orderItemService, GoodsClient goodsClient, GoodsSkuClient goodsSkuClient, AmqpTemplate rabbit, StringRedisTemplate srt) {
        super();
        this.idService = idService;
        this.orderItemService = orderItemService;
        this.goodsClient = goodsClient;
        this.goodsSkuClient = goodsSkuClient;
        this.rabbit = rabbit;
        this.srt = srt;
    }

    @Override
    public Integer submit(OrderDTO dto) {
        if (log.isDebugEnabled()) {
            log.debug("【订单提交】,订单内容：{}", JsonUtil.toJson(dto));
        }
        Integer totalAmount = 0;
        Integer postageAmount = 0;
        Integer discountAmount = 0;
        if (dto.getOrderId() == null) {
            dto.setOrderId(idService.nextId());
        }
        for (OrderDTO.OrderItemDTO itemDTO : dto.getItem()) {
            Goods goods = goodsClient.getBySkuId(itemDTO.getSkuId());
            GoodsSku sku = goodsSkuClient.get(itemDTO.getSkuId());
            Assert.buyCheck(goods);
            if (sku.getQty() < itemDTO.getQty()) {
                throw new RuntimeException("库存不足");
            }
            if (itemDTO.getCouponId() != null && itemDTO.getCouponId() > 0) {
                // TODO 优惠券校验
            }
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
        orderInfo.setStatus(OrderStatus.PENDING_CHECK.getCode());
        int res = this.save(orderInfo);
        if (res > 0) {
            rabbit.convertAndSend(Const.TOPIC_ORDER_CHECK, JsonUtil.toJson(dto));
        }
        return res;
    }

    @Override
    public Integer storageShortage(Long orderId) {
        return mapper.storageShortage(orderId);
    }

    @Override
    public Integer pendingPayment(Long orderId) {
        return mapper.pendingPayment(orderId);
    }

    @Override
    public Integer expireClose(Long orderId) {
        return mapper.expireClose(orderId);
    }
}
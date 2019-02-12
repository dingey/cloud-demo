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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

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

    public OrderInfoServiceImpl(IdService idService, OrderItemService orderItemService, GoodsClient goodsClient, GoodsSkuClient goodsSkuClient, AmqpTemplate rabbit) {
        super();
        this.idService = idService;
        this.orderItemService = orderItemService;
        this.goodsClient = goodsClient;
        this.goodsSkuClient = goodsSkuClient;
        this.rabbit = rabbit;
    }

    @Override
    public Integer submit(OrderDTO dto) {
        if (log.isDebugEnabled()) {
            log.debug("【订单提交】,订单内容：{}", JsonUtil.toJson(dto));
        }
        Integer totalAmount = 0;
        Integer postageAmount = 0;
        Integer discountAmount = 0;
        boolean fail = false;
        HashSet<Long> tmp = null;
        for (OrderDTO.OrderItemDTO itemDTO : dto.getItem()) {
            Goods goods = goodsClient.getBySkuId(itemDTO.getSkuId());
            GoodsSku sku = goodsSkuClient.get(itemDTO.getSkuId());
            Assert.buyCheck(goods);
            if (itemDTO.getCouponId() != null && itemDTO.getCouponId() > 0) {
                // TODO 优惠券校验
            }
            Integer reduceInventory = goodsSkuClient.reduceInventory(itemDTO.getSkuId(), itemDTO.getQty());
            if (reduceInventory < 1) {
                fail = true;
                if (tmp == null) {
                    tmp = new HashSet<>();
                }
                tmp.add(itemDTO.getSkuId());
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
        orderInfo.setId(dto.getOrderId() == null ? idService.nextId() : dto.getOrderId());
        orderInfo.setUserId(dto.getUserId());
        orderInfo.setDiscountAmount(discountAmount);
        orderInfo.setPostageAmount(postageAmount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setStatus(fail ? OrderStatus.CLOSED.getCode() : OrderStatus.PENDING_PAYMENT.getCode());
        int res = this.save(orderInfo);
        if (res > 0 && !fail) {
            rabbit.convertAndSend(Const.TOPIC_ORDER_CHECK, JsonUtil.toJson(dto));
        } else {// 回退库存
            for (OrderDTO.OrderItemDTO it : dto.getItem()) {
                if (!tmp.contains(it.getSkuId())) goodsSkuClient.increaseInventory(it.getSkuId(), it.getQty());
            }
        }
        return res;
    }

}
package com.d.goods.listener;

import com.d.base.Const;
import com.d.enums.OrderStatus;
import com.d.goods.service.GoodsSkuService;
import com.d.order.dto.OrderDTO;
import com.d.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 订单扣减库存监听器
 */
@Slf4j
@Component
@RabbitListener(queues = Const.TOPIC_ORDER_CHECK)
public class DeductionInventoryListener {
    private final GoodsSkuService goodsSkuService;
    private final StringRedisTemplate srt;
    private final AmqpTemplate rabbit;

    @Autowired
    public DeductionInventoryListener(GoodsSkuService goodsSkuService, StringRedisTemplate srt, AmqpTemplate rabbit) {
        this.goodsSkuService = goodsSkuService;
        this.srt = srt;
        this.rabbit = rabbit;
    }

    @RabbitHandler
    public void process(String message) {
        OrderDTO dto = JsonUtil.single().fromJson(message, OrderDTO.class);
        boolean fail = false;
        if (srt.opsForHash().putIfAbsent(Const.CACHE_KEY_ORDER + dto.getOrderId(), "status", OrderStatus.PENDING_CHECK.getCode())) {
            for (OrderDTO.OrderItemDTO it : dto.getItem()) {
                Long inventory = goodsSkuService.reduceInventory(it.getSkuId(), it.getQty(), dto.getOrderId());
                if (inventory < 0L) {
                    if (log.isDebugEnabled()) {
                        log.debug("【库存不足】【商品SKU：{}】【扣减数量：{}】", it.getSkuId(), it.getQty());
                    }
                    fail = true;
                }
            }
            if (fail) {
                for (OrderDTO.OrderItemDTO it : dto.getItem()) {
                    goodsSkuService.increaseInventory(it.getSkuId(), it.getQty(), dto.getOrderId());
                }
                rabbit.convertAndSend(Const.TOPIC_ORDER_CHECK_FAIL, dto.getOrderId());
            } else {
                rabbit.convertAndSend(Const.TOPIC_ORDER_CHECK_SUCCESS, dto.getOrderId());
            }
            if (log.isDebugEnabled()) {
                log.debug("【订单扣减库存监听器】【订单信息：{}】【结果：{}】", message, fail ? "失败" : "成功");
            }
        }
    }
}

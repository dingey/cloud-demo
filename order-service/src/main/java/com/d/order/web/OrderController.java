package com.d.order.web;

import com.d.base.Const;
import com.d.base.Result;
import com.d.common.service.IdService;
import com.d.order.dto.OrderDTO;
import com.d.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther d
 */
@RestController
public class OrderController {
    private final IdService idService;
    private final AmqpTemplate rabbit;
    private final StringRedisTemplate srt;

    @Autowired
    public OrderController(IdService idService, AmqpTemplate rabbit, StringRedisTemplate srt) {
        this.idService = idService;
        this.rabbit = rabbit;
        this.srt = srt;
    }

    @ApiOperation("订单提交")
    @PostMapping(path = "/order/submit")
    public Result submit(@RequestBody OrderDTO orderDTO) {
        orderDTO.setOrderId(idService.nextId());
        rabbit.convertAndSend(Const.TOPIC_ORDER_CREATE, JsonUtil.single().toJson(orderDTO));
        return Result.success("服务器处理中，请稍后");
    }

    @ApiOperation("订单状态查询")
    @PostMapping(path = "/order/query")
    public Result query(Long orderId) {
        Object status = srt.opsForHash().get(Const.CACHE_KEY_ORDER + orderId, "status");
        return Result.success(status);
    }
}

package com.d.order.web;

import com.d.base.Const;
import com.d.base.Result;
import com.d.base.ResultCode;
import com.d.common.service.IdService;
import com.d.order.config.OrderConfig;
import com.d.order.dto.OrderDTO;
import com.d.order.service.OrderInfoService;
import com.d.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther d
 */
@RestController
public class OrderController {
    private final IdService idService;
    private final OrderConfig orderConfig;
    private final OrderInfoService orderInfoService;
    private final AmqpTemplate rabbit;
    private final StringRedisTemplate srt;

    @Autowired
    public OrderController(IdService idService, OrderConfig orderConfig, OrderInfoService orderInfoService, AmqpTemplate rabbit, StringRedisTemplate srt) {
        this.idService = idService;
        this.orderConfig = orderConfig;
        this.orderInfoService = orderInfoService;
        this.rabbit = rabbit;
        this.srt = srt;
    }

    @ApiOperation("订单提交")
    @PostMapping(path = "/order/submit")
    public Result<Object> submit(@RequestBody OrderDTO orderDTO) {
        Long id = idService.nextId();
        if (orderConfig.isOrderAsyncSubmit()) {
            orderDTO.setOrderId(idService.nextId());
            rabbit.convertAndSend(Const.TOPIC_ORDER_CREATE, JsonUtil.single().toJson(orderDTO));
            return Result.successAsync(id);
        } else {
            Integer submit = orderInfoService.submit(orderDTO);
            return submit > 0 ? Result.success(id) : Result.fail(ResultCode.SERVER_ERROR);
        }
    }

    @ApiOperation("订单状态查询")
    @GetMapping(path = "/order/query")
    public Result<Object> query(Long orderId) {
        Object status = srt.opsForHash().get(Const.CACHE_KEY_ORDER + orderId, "status");
        return Result.success(status);
    }

    @ApiOperation("订单超时关闭")
    @PostMapping(path = "/order/expire")
    public Result<Object> expire(Long orderId) {
        return Result.success(orderInfoService.expireClose(orderId));
    }
}

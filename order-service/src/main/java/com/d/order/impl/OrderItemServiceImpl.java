package com.d.order.impl;

import com.d.order.service.OrderItemService;
import com.d.base.BaseServiceImpl;
import com.d.order.mapper.OrderItemMapper;
import com.d.order.entity.OrderItem;
import org.springframework.stereotype.Service;
/**
 * 订单明细service
 * @author d
 * @date 2019-01-16 20:51
 */
@Service("orderItemService")
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItemMapper,OrderItem> implements OrderItemService {

}
package com.d.order.service;

import com.d.base.BaseService;
import com.d.order.dto.OrderDTO;
import com.d.order.entity.OrderInfo;
/**
 * 订单信息service
 * @author d
 * @date 2019-01-16 20:51
 */
public interface OrderInfoService extends BaseService<OrderInfo> {
	Integer submit(OrderDTO order);
}
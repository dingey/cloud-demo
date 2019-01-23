package com.d.order.impl;

import com.d.order.service.OrderInfoService;
import com.d.base.BaseServiceImpl;
import com.d.order.mapper.OrderInfoMapper;
import com.d.order.entity.OrderInfo;
import org.springframework.stereotype.Service;
/**
 * 订单信息service
 * @author d
 * @date 2019-01-16 20:51
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfoMapper,OrderInfo> implements OrderInfoService {

}
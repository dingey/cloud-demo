package com.d.order.mapper;

import com.d.base.BaseMapper;
import com.d.order.entity.OrderInfo;
import org.apache.ibatis.annotations.Update;

/**
 * 订单信息Mapper接口
 *
 * @author d
 * @date 2019-01-16 20:51
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    @Update("update order_info set status=9 where id=#{orderId} and status=0")
    Integer storageShortage(Long orderId);

    @Update("update order_info set status=1 where id=#{orderId} and status=0")
    Integer pendingPayment(Long orderId);

    @Update("update order_info set status=10 where id=#{orderId} and status=1")
    Integer expireClose(Long orderId);
}
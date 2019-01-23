package com.d.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther d
 */
@Data
public class OrderDTO implements Serializable {
    private Long orderId;
    private Long userId;
    private Long storeId;
    private Long couponId;
    private List<OrderItemDTO> item;

    @Data
    public static class OrderItemDTO implements Serializable {
        private Long skuId;
        private Integer qty;
        private Long couponId;
    }
}

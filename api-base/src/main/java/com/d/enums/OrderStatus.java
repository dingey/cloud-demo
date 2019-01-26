package com.d.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_CHECK(0, "待校验"),
    PENDING_PAYMENT(1, "待支付"),
    PAIED(2, "已支付"),
    PENDING_DELIVERY(3, "待发货"),
    SHIPPED(4, "已发货"),
    PENDING_EVALUATION(5, "待评价"),
    COMPLETED(6, "已完成"),
    CANCELLED(7, "取消"),
    REFUNDED(8, "已退款"),
    INVENTORY_SHORTAGE(9, "库存不足");

    private int code;
    private String name;

    OrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderStatus valueOf(int code) {
        for (OrderStatus os : values()) {
            if (os.getCode() == code)
                return os;
        }
        throw new RuntimeException("不存在的订单状态");
    }
}

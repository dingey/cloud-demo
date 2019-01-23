package com.d.order.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;

@Data
@ApiModel("订单明细")
public class OrderItem extends BaseEntity<OrderItem> {
    private static final long serialVersionUID = 1085519195673722880L;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("SKUid")
    private Integer goodsSkuId;
    @ApiModelProperty("订单父id")
    private Long parentOrderId;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("数量")
    private Integer qty;
    @ApiModelProperty("单价：分")
    private Integer unitPrice;
    @ApiModelProperty("总金额：分")
    private Integer totalAmount;
    private Integer discountAmount;
    private Integer couponId;
}
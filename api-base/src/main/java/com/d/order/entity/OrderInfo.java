package com.d.order.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;

@Data
@ApiModel("订单信息")
public class OrderInfo extends BaseEntity<OrderInfo> {
	private static final long serialVersionUID = 1085519195656945664L;
    @ApiModelProperty("用户id")
    private Long userId;
    private Long userAddrId;
    @ApiModelProperty("是否拆单：0否；1是")
    private Integer splitOrder;
    @ApiModelProperty("父订单:拆单下才存在")
    private Long parentId;
    private Long couponId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("描述")
    private String desc;
    @ApiModelProperty("总金额")
    private Integer totalAmount;
    @ApiModelProperty("优惠金额")
    private Integer discountAmount;
    @ApiModelProperty("邮费金额")
    private Integer postageAmount;
    @ApiModelProperty("实付金额")
    private Integer payAmount;
    @ApiModelProperty("快递单号")
    private String expressNo;
    @ApiModelProperty("订单状态：0待支付；1已支付；2待发货；3已发货；4待评价；5完成；6取消；7已退款")
    private Integer status;
    @ApiModelProperty("订单类型")
    private Integer type;
    @ApiModelProperty("收件人姓名")
    private String name;
    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("区")
    private String region;
    @ApiModelProperty("地址")
    private String address;
}
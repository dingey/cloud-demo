package com.d.pay.entity;

import com.d.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 支付单
 * @author d
 * @date 2019-01-16 21:09
 */
@Data
@ApiModel("支付单")
public class PayInfo extends BaseEntity<PayInfo> {
	private static final long serialVersionUID = 1085524371025428480L;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("支付方式/渠道：0余额支付；20支付宝；30微信；40银联")
    private Integer payMethod;
    @ApiModelProperty("支付账户：支付宝账号；微信号；银行卡号")
    private String payAccount;
    @ApiModelProperty("支付单号、流水号")
    private String payNo;
    @ApiModelProperty("支付金额")
    private Integer payAmount;
    @ApiModelProperty("交易类型")
    private String payType;
    @ApiModelProperty("交易类型")
    private String tradeType;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("支付状态：0待支付；1已支付；2失败；3已取消；4已关闭")
    private Integer payStatus;
    @ApiModelProperty("支付时间")
    private Date payTime;
}
package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品属性
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品属性")
public class GoodsAttr extends BaseEntity<GoodsAttr> {
	private static final long serialVersionUID = 1085522620431990784L;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("名称")
    private String name;
}
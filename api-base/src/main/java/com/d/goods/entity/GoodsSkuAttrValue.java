package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品SKU属性值
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品SKU属性值")
public class GoodsSkuAttrValue extends BaseEntity<GoodsSkuAttrValue> {
	private static final long serialVersionUID = 1085522620511682560L;
    @ApiModelProperty("商品SKUid")
    private Integer goodsSkuId;
    @ApiModelProperty("商品属性值id")
    private Integer goodsAttrValueId;
}
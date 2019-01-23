package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品属性值
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品属性值")
public class GoodsAttrValue extends BaseEntity<GoodsAttrValue> {
	private static final long serialVersionUID = 1085522620444573696L;
    @ApiModelProperty("商品属性id")
    private Integer goodsAttrId;
    @ApiModelProperty("名称")
    private String name;
}
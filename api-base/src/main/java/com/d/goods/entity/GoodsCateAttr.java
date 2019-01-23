package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品分类可选属性
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品分类可选属性")
public class GoodsCateAttr extends BaseEntity<GoodsCateAttr> {
	private static final long serialVersionUID = 1085522620469739520L;
    @ApiModelProperty("分类id")
    private Integer goodsCateId;
    @ApiModelProperty("名称")
    private String name;
}
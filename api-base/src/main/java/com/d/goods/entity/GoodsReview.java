package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品评价
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品评价")
public class GoodsReview extends BaseEntity<GoodsReview> {
	private static final long serialVersionUID = 1085522620490711040L;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("商品id")
    private Integer goodsId;
}
package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品收藏
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品收藏")
public class GoodsCollect extends BaseEntity<GoodsCollect> {
	private static final long serialVersionUID = 1085522620482322432L;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("商品id")
    private Integer goodsId;
}
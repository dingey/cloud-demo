package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品SKU
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品SKU")
public class GoodsSku extends BaseEntity<GoodsSku> {
	private static final long serialVersionUID = 1085522620503293952L;
    @ApiModelProperty("店铺id")
    private Long storeId;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("商品编码")
    private String goodsCode;
    @ApiModelProperty("商品条形码")
    private String goodsBarcode;
    @ApiModelProperty("主图、封面")
    private String cover;
    @ApiModelProperty("库存")
    private Integer qty;
    @ApiModelProperty("价格")
    private Integer price;
}
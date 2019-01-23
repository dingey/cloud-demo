package com.d.goods.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.d.base.BaseEntity;
/**
 * 商品分类
 * @author d
 * @date 2019-01-16 21:02
 */
@Data
@ApiModel("商品分类")
public class GoodsCate extends BaseEntity<GoodsCate> {
	private static final long serialVersionUID = 1085522620457156608L;
    @ApiModelProperty("父类")
    private Integer pid;
    @ApiModelProperty("名称")
    private String name;
}
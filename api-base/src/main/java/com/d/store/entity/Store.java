package com.d.store.entity;

import com.d.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺
 *
 * @author d
 * @date 2019-01-16 21:06
 */
@Data
@ApiModel("店铺")
public class Store extends BaseEntity<Store> {
    private static final long serialVersionUID = 1085523570735448064L;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("行业id")
    private Integer industryId;
    @ApiModelProperty("店铺名称")
    private String storeName;
    @ApiModelProperty("店铺简介")
    private String storeIntroduction;
    @ApiModelProperty("店铺LOGO")
    private String storeLogo;
}
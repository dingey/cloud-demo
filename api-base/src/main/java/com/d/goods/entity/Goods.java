package com.d.goods.entity;

import com.d.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@ApiModel("商品")
@Document(indexName = "goods", type = "goods", shards = 1, replicas = 0, refreshInterval = "-1")
public class Goods extends BaseEntity<Goods> {
	private static final long serialVersionUID = 1086148322999140352L;
    @ApiModelProperty("店铺id")
    private Integer storeId;
    @ApiModelProperty("商品分类")
    private Integer goodsCateId;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品简介")
    private String desc;
    @ApiModelProperty("原价")
    private Integer originalPrice;
    @ApiModelProperty("销售价、优惠价")
    private Integer salePrice;
    @ApiModelProperty("库存")
    private Integer qty;
    @ApiModelProperty("商品封面")
    private String cover;
    @ApiModelProperty("商品轮播图")
    private String carousel;
    @ApiModelProperty("商品详情：富文本")
    private String detail;
    @ApiModelProperty("上架状态:0下架；1上架")
    private Integer shelfStatus;
    @ApiModelProperty("推荐状态：0不推荐；1推荐")
    private Integer recommendStatus;
    @ApiModelProperty("开始时间")
    private java.util.Date startTime;
    @ApiModelProperty("结束时间")
    private java.util.Date endTime;
    @ApiModelProperty("邮费类型：0包邮；1区域运费；")
    private Integer postageType;
}
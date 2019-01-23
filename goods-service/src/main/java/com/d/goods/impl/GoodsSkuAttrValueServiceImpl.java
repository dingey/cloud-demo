package com.d.goods.impl;

import com.d.goods.service.GoodsSkuAttrValueService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsSkuAttrValueMapper;
import com.d.goods.entity.GoodsSkuAttrValue;
import org.springframework.stereotype.Service;
/**
 * 商品SKU属性值service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsSkuAttrValueService")
public class GoodsSkuAttrValueServiceImpl extends BaseServiceImpl<GoodsSkuAttrValueMapper,GoodsSkuAttrValue> implements GoodsSkuAttrValueService {

}
package com.d.goods.impl;

import com.d.goods.service.GoodsSkuService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsSkuMapper;
import com.d.goods.entity.GoodsSku;
import org.springframework.stereotype.Service;
/**
 * 商品SKUservice
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsSkuService")
public class GoodsSkuServiceImpl extends BaseServiceImpl<GoodsSkuMapper,GoodsSku> implements GoodsSkuService {

}
package com.d.goods.impl;

import com.d.goods.service.GoodsService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsMapper;
import com.d.goods.entity.Goods;
import org.springframework.stereotype.Service;
/**
 * 商品service
 * @author d
 * @date 2019-01-18 14:28
 */
@Service("goodsService")
public class GoodsServiceImpl extends BaseServiceImpl<GoodsMapper,Goods> implements GoodsService {

}
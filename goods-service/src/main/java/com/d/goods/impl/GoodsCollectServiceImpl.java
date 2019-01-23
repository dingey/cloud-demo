package com.d.goods.impl;

import com.d.goods.service.GoodsCollectService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsCollectMapper;
import com.d.goods.entity.GoodsCollect;
import org.springframework.stereotype.Service;
/**
 * 商品收藏service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsCollectService")
public class GoodsCollectServiceImpl extends BaseServiceImpl<GoodsCollectMapper,GoodsCollect> implements GoodsCollectService {

}
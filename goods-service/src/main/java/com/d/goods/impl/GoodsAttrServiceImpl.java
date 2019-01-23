package com.d.goods.impl;

import com.d.goods.service.GoodsAttrService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsAttrMapper;
import com.d.goods.entity.GoodsAttr;
import org.springframework.stereotype.Service;
/**
 * 商品属性service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsAttrService")
public class GoodsAttrServiceImpl extends BaseServiceImpl<GoodsAttrMapper,GoodsAttr> implements GoodsAttrService {

}
package com.d.goods.impl;

import com.d.goods.service.GoodsAttrValueService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsAttrValueMapper;
import com.d.goods.entity.GoodsAttrValue;
import org.springframework.stereotype.Service;
/**
 * 商品属性值service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsAttrValueService")
public class GoodsAttrValueServiceImpl extends BaseServiceImpl<GoodsAttrValueMapper,GoodsAttrValue> implements GoodsAttrValueService {

}
package com.d.goods.impl;

import com.d.goods.service.GoodsCateAttrService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsCateAttrMapper;
import com.d.goods.entity.GoodsCateAttr;
import org.springframework.stereotype.Service;
/**
 * 商品分类可选属性service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsCateAttrService")
public class GoodsCateAttrServiceImpl extends BaseServiceImpl<GoodsCateAttrMapper,GoodsCateAttr> implements GoodsCateAttrService {

}
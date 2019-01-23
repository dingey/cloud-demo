package com.d.goods.impl;

import com.d.goods.service.GoodsCateService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsCateMapper;
import com.d.goods.entity.GoodsCate;
import org.springframework.stereotype.Service;
/**
 * 商品分类service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsCateService")
public class GoodsCateServiceImpl extends BaseServiceImpl<GoodsCateMapper,GoodsCate> implements GoodsCateService {

}
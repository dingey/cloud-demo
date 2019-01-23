package com.d.goods.impl;

import com.d.goods.service.GoodsReviewService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsReviewMapper;
import com.d.goods.entity.GoodsReview;
import org.springframework.stereotype.Service;
/**
 * 商品评价service
 * @author d
 * @date 2019-01-16 21:02
 */
@Service("goodsReviewService")
public class GoodsReviewServiceImpl extends BaseServiceImpl<GoodsReviewMapper,GoodsReview> implements GoodsReviewService {

}
package com.d.goods.impl;

import com.d.base.Const;
import com.d.goods.service.GoodsSkuService;
import com.d.base.BaseServiceImpl;
import com.d.goods.mapper.GoodsSkuMapper;
import com.d.goods.entity.GoodsSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 商品SKUservice
 *
 * @author d
 * @date 2019-01-16 21:02
 */
@Service
public class GoodsSkuServiceImpl extends BaseServiceImpl<GoodsSkuMapper, GoodsSku> implements GoodsSkuService {
    private final StringRedisTemplate srt;

    @Autowired
    public GoodsSkuServiceImpl(StringRedisTemplate srt) {
        this.srt = srt;
    }

    @Override
    public Long reduceInventory(Long skuId, Integer qty) {
        if (srt.hasKey(Const.CACHE_KEY_STOCK + skuId)) {
            return srt.opsForValue().increment(Const.CACHE_KEY_STOCK + skuId, -qty);
        } else {
            return mapper.reduceInventory(skuId, qty);
        }
    }

    @Override
    public Long increaseInventory(Long skuId, Integer qty) {
        if (srt.hasKey(Const.CACHE_KEY_STOCK + skuId)) {
            return srt.opsForValue().increment(Const.CACHE_KEY_STOCK + skuId, qty);
        } else {
            return mapper.increaseInventory(skuId, qty);
        }
    }
}
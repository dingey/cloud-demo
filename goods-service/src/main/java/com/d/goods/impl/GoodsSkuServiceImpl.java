package com.d.goods.impl;

import com.d.base.Const;
import com.d.goods.entity.InventoryRecord;
import com.d.goods.service.InventoryRecordService;
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
    private final InventoryRecordService inventoryRecordService;

    @Autowired
    public GoodsSkuServiceImpl(StringRedisTemplate srt, InventoryRecordService inventoryRecordService) {
        this.srt = srt;
        this.inventoryRecordService = inventoryRecordService;
    }

    @Override
    public Long reduceInventory(Long skuId, Integer qty, Long orderId) {
        InventoryRecord record = new InventoryRecord();
        record.setSkuId(skuId);
        record.setOrderId(orderId);
        record.setQty(-qty);
        inventoryRecordService.save(record);
        Long inventory = mapper.reduceInventory(skuId, qty);
        if (srt.hasKey(Const.CACHE_KEY_STOCK + skuId)) {
            srt.opsForValue().increment(Const.CACHE_KEY_STOCK + skuId, -qty);
        }
        return inventory;
    }

    @Override
    public Long increaseInventory(Long skuId, Integer qty, Long orderId) {
        InventoryRecord record = new InventoryRecord();
        record.setSkuId(skuId);
        record.setOrderId(orderId);
        record.setQty(qty);
        inventoryRecordService.save(record);
        Long inventory = mapper.increaseInventory(skuId, qty);
        if (srt.hasKey(Const.CACHE_KEY_STOCK + skuId)) {
            srt.opsForValue().increment(Const.CACHE_KEY_STOCK + skuId, qty);
        }
        return inventory;
    }
}
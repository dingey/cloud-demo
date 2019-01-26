package com.d.goods.web;

import com.d.goods.entity.Goods;
import com.d.goods.entity.GoodsSku;
import com.d.goods.service.GoodsService;
import com.d.goods.service.GoodsSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSkuService goodsSkuService;

    @GetMapping(path = "/goods/{id}")
    public Goods get(@PathVariable("id") Long id) {
        return goodsService.getCache(id);
    }

    @GetMapping(path = "/goods/sku/{id}")
    public Goods getBySku(@PathVariable("id") Long id) {
        GoodsSku sku = goodsSkuService.get(id);
        return goodsService.getCache(sku.getGoodsId());
    }
}

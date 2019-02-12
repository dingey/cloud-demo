package com.d.goods.web;

import com.d.goods.entity.GoodsSku;
import com.d.goods.service.GoodsSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsSkuController {
    private final GoodsSkuService goodsSkuService;

    @Autowired
    public GoodsSkuController(GoodsSkuService goodsSkuService) {
        this.goodsSkuService = goodsSkuService;
    }

    @GetMapping(path = "/goodsSku/{id}")
    public GoodsSku get(@PathVariable("id") Long id) {
        return goodsSkuService.get(id);
    }

    @PostMapping(path = "/goodsSku/reduce")
    public Long reduce(Long id, Integer qty) {
        return goodsSkuService.reduceInventory(id, qty);
    }

    @PostMapping(path = "/goodsSku/increase")
    public Long increase(Long id, Integer qty) {
        return goodsSkuService.increaseInventory(id, qty);
    }
}

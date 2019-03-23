package com.d.client.goods;

import com.d.goods.entity.GoodsSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @auther d
 */
@FeignClient(value = "goodsSkuClient")
public interface GoodsSkuClient {
    @RequestMapping(path = "/goodsSku/{id}", method = RequestMethod.GET)
    GoodsSku get(@PathVariable("id") Long id);

    @RequestMapping(path = "/goodsSku/reduce", method = RequestMethod.POST)
    Integer reduceInventory(@RequestParam("id") Long id, @RequestParam("qty") Integer qty, @RequestParam("orderId") Long orderId);

    @RequestMapping(path = "/goodsSku/increase", method = RequestMethod.POST)
    Integer increaseInventory(@RequestParam("id") Long id, @RequestParam("qty") Integer qty, @RequestParam("orderId") Long orderId);
}

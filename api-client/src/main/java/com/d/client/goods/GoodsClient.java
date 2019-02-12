package com.d.client.goods;

import com.d.goods.entity.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @auther d
 */
@FeignClient(value = "GOODS-SERVICE")
public interface GoodsClient {
    @GetMapping("/goods/{id}")
    Goods get(@PathVariable("id") Long id);

    @GetMapping("/goods/sku/{id}")
    Goods getBySkuId(@PathVariable("id") Long id);
}

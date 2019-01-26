package com.d.client.goods;

import com.d.goods.entity.GoodsSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @auther d
 */
@FeignClient(value = "SERVICE-GOODS")
public interface GoodsSkuClient {
    @GetMapping("/goodsSku/{id}")
    GoodsSku get(Long id);
}

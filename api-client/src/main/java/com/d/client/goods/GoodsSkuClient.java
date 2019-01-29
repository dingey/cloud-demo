package com.d.client.goods;

import com.d.goods.entity.GoodsSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @auther d
 */
@FeignClient(value = "SERVICE-GOODS")
public interface GoodsSkuClient {
	@GetMapping("/goodsSku/{id}")
	GoodsSku get(Long id);

	@PostMapping("/goodsSku/reduce")
	Integer reduceInventory(Long id, Integer qty);
	
	@PostMapping("/goodsSku/increase")
	Integer increaseInventory(Long id, Integer qty);
}

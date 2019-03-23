package com.d.client.search;

import com.d.goods.entity.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "SEARCH-SERVICE", fallbackFactory = SearchClientFallback.class)
public interface SearchClient {
    @RequestMapping(path = "/search/goods", method = RequestMethod.GET)
    Page<Goods> searchGoods(@RequestParam String name, @RequestParam Integer page, @RequestParam Integer size);

    @RequestMapping(path = "/search/goods", method = RequestMethod.POST)
    Integer saveGoods(Goods goods);

    @RequestMapping(path = "/search/all", method = RequestMethod.GET)
    List<Map<String, Object>> searchAll();
}

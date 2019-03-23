package com.d.search.web;

import com.d.goods.entity.Goods;
import com.d.search.service.GoodsEsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class SearchController {
    private final ElasticsearchTemplate et;
    private final GoodsEsService goodsEsService;

    @Autowired
    public SearchController(ElasticsearchTemplate et, GoodsEsService goodsEsService) {
        this.et = et;
        this.goodsEsService = goodsEsService;
    }

    /**
     * 按字段查询
     *
     * @param name 商品名称
     * @param page 页码
     * @param size 大小
     * @return 分页的数据
     */
    @GetMapping(path = "/search/goods")
    public Page<Goods> list(String name, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return goodsEsService.search(name, page, size);
    }

    @PostMapping(path = "/search/goods")
    public Object list(Goods goods) {
        return goodsEsService.save(goods);
    }

    /**
     * 查询所有
     */
    @GetMapping("/search/all")
    public List<Map<String, Object>> searchAll() {
        Client client = et.getClient();
        SearchRequestBuilder srb = client.prepareSearch("goods").setTypes("goods");
        SearchResponse sr = srb.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet(); // 查询所有
        SearchHits hits = sr.getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap();
            list.add(source);
            log.debug(hit.getSourceAsString());
        }
        return list;
    }
}

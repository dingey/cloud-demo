package com.d.search.dao;

import com.d.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
    List<Goods> findByName(String name);

    Page<Goods> findByNameLikeOrderById(String name, Pageable pageable);
}
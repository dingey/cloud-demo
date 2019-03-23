package com.d.search.service;

import com.d.goods.entity.Goods;
import org.springframework.data.domain.Page;

public interface GoodsEsService {
    int save(Goods goodsEs);

    Page<Goods> search(String name, Integer page, Integer size);
}

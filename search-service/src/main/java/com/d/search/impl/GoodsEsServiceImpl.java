package com.d.search.impl;

import com.d.goods.entity.Goods;
import com.d.search.dao.GoodsRepository;
import com.d.search.service.GoodsEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsEsServiceImpl implements GoodsEsService {
    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsEsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public int save(Goods goods) {
        goodsRepository.save(goods);
        return 1;
    }

    @Override
    public Page<Goods> search(String name, Integer page, Integer size) {
        return goodsRepository.findByNameLikeOrderById(name, PageRequest.of(page, size));
    }

}

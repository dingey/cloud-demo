package com.d.goods.web;

import com.d.base.Result;
import com.d.goods.config.FormModelResolver;
import com.d.goods.dto.GoodsDTO;
import com.d.goods.entity.Goods;
import com.d.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.function.Function;
import java.util.stream.Collector;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping(path = "/goods/{id}")
    public Goods get(@PathVariable("id") Long id) {
        return goodsService.getCache(id);
    }

    @PostMapping(path = "/goods/save")
    public Object save(@Valid @FormModelResolver.FormModel GoodsDTO goods, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder s = new StringBuilder();
            result.getAllErrors().forEach(c -> {
                s.append(c.getDefaultMessage());
            });
            return Result.fail(s.toString());
        }
        return 1;
    }
}

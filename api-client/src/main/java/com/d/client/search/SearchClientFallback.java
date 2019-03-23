package com.d.client.search;


import com.d.goods.entity.Goods;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SearchClientFallback implements FallbackFactory<SearchClient> {
    @Override
    public SearchClient create(Throwable throwable) {
        return new SearchClient() {

            @Override
            public Page<Goods> searchGoods(String name, Integer page, Integer size) {
                log.error("服务不可用");
                return null;
            }

            @Override
            public Integer saveGoods(Goods goods) {
                log.error("服务不可用");
                return null;
            }

            @Override
            public List<Map<String, Object>> searchAll() {
                log.error("服务不可用");
                return null;
            }
        };
    }
}

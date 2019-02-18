package com.d.client.order;

import com.d.base.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "ORDER-SERVICE")
public interface OrderClient {
    @RequestMapping(path = "/order/expire", method = RequestMethod.POST)
    Result<Integer> expire(@RequestParam Long id);
}

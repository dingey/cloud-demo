package com.d.util;

import com.d.goods.entity.Goods;

import java.util.Date;

public class Assert {
    //商品可买校验
    public static void buyCheck(Goods goods) {
        Date now = new Date();
        if (goods.getStartTime().after(now)) {
            throw new RuntimeException("商品开始时间未到，不能购买");
        }
        if (goods.getEndTime().before(now)) {
            throw new RuntimeException("商品结束时间已过，不能购买");
        }
        if (goods.getShelfStatus() != 1) {
            throw new RuntimeException("商品未上架，不能购买");
        }
    }
}

package com.d.util;

import com.d.goods.entity.Goods;
import org.springframework.util.StringUtils;

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

    public static void notNull(Object o, String message) {
        if (o == null) throw new RuntimeException(message);
    }

    public static void verifyLogin() {
        String header = ServletUtil.getRequest().getHeader("user-token");
        if (StringUtils.isEmpty(header)) {
            throw new RuntimeException("请先登录");
        }
        UserToken token = UserToken.fromTokenString(header);
        if (token == null || !token.valid()) {
            throw new RuntimeException("请先登录");
        }
    }
}

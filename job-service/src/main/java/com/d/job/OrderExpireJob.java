package com.d.job;

import com.d.base.Const;
import com.d.client.order.OrderClient;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

public class OrderExpireJob implements InterruptableJob {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private StringRedisTemplate srt;
    private boolean run = true;

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        run = false;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Set<String> range = srt.opsForZSet().rangeByScore(Const.CACHE_KEY_ORDER_EXPIRE, 0, System.currentTimeMillis() / 1000);
        for (String s : range) {
            if (!run)
                return;
            orderClient.expire(Long.valueOf(s));
            srt.opsForZSet().remove(Const.CACHE_KEY_ORDER_EXPIRE, s);
        }
    }
}

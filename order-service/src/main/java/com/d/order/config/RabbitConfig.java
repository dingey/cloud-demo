package com.d.order.config;

import com.d.base.Const;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther d
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue orderQueue() {
        return new Queue(Const.TOPIC_ORDER_CREATE);
    }
}

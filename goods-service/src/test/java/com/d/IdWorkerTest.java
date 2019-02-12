package com.d;

import com.di.kit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @auther ding
 * @date 2019/1/26 17:19
 * @since 0.0.1
 */
@Slf4j
public class IdWorkerTest {
    @Test
    public void test() {
        IdWorker worker1 = new IdWorker(1L, 0);
        IdWorker worker2 = new IdWorker(2L, 0);
        int size = 32;
        log.info("worker1:" + worker1.nextIdSync() % size + " worker2:" + worker2.nextIdSync() % size);
        log.info("worker1:" + worker1.nextIdSync() % size + " worker2:" + worker2.nextIdSync() % size);
        log.info("worker1:" + worker1.nextIdSync() % size + " worker2:" + worker2.nextIdSync() % size);
        log.info("worker1:" + worker1.nextIdSync() % size + " worker2:" + worker2.nextIdSync() % size);


    }
}

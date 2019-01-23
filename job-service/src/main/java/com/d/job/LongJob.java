package com.d.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
@DisallowConcurrentExecution
public class LongJob implements InterruptableJob {
    private boolean interrupt = false;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("耗时任务运行开始。。。");
        try {
            for (int i = 0; i < 9 && !interrupt; i++) {
                Thread.sleep(1000L);
            }
            if (interrupt) {
                log.info("耗时任务被打断。。。。。");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("耗时任务运行完成。。。");
    }

    @Override
    public void interrupt() {
        interrupt = true;
    }
}
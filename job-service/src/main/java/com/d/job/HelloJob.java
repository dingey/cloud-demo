package com.d.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
public class HelloJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("hello...");
    }
}
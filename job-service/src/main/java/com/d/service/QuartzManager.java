package com.d.service;

import com.di.kit.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Service
@SuppressWarnings("all")
public class QuartzManager {
    private final Scheduler scheduler;

    @Autowired
    public QuartzManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 增加一个job
     *  
     *
     * @param jobClass                任务实现类
     * @param jobName                 任务名称
     * @param jobGroupName            任务组名
     * @param jobCron                 cron表达式(如：0/5 * * * * ? )
     */
    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, String jobCron) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobCron)).startNow().build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 创建or更新任务，存在则更新不存在创建
     *  
     *
     * @param jobClass                任务类
     * @param jobName                 任务名称
     * @param jobGroupName            任务组名称
     * @param jobCron                 cron表达式
     */
    public boolean addJobIfNotExists(Class<? extends Job> jobClass, String jobName, String jobGroupName, String jobCron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                addJob(jobClass, jobName, jobGroupName, jobCron);
                return true;
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 创建or更新任务，存在则更新不存在创建
     *  
     *
     * @param jobClass                任务类
     * @param jobName                 任务名称
     * @param jobGroupName            任务组名称
     * @param jobCron                 cron表达式
     */
    public void addOrUpdateJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, String jobCron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                addJob(jobClass, jobName, jobGroupName, jobCron);
            } else {
                if (trigger.getCronExpression().equals(jobCron)) {
                    return;
                }
                updateJob(jobName, jobGroupName, jobCron);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     *  
     *
     * @param jobClass
     * @param jobName
     * @param jobGroupName
     * @param jobTime
     */
    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, int jobTime) {
        addJob(jobClass, jobName, jobGroupName, jobTime, -1);
    }

    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, int jobTime, int jobTimes) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)// 任务名称和组构成任务key
                    .build();
            // 使用simpleTrigger规则
            Trigger trigger = null;
            if (jobTimes < 0) {
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
                        .startNow().build();
            } else {
                trigger = TriggerBuilder
                        .newTrigger().withIdentity(jobName, jobGroupName).withSchedule(SimpleScheduleBuilder
                                .repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
                        .startNow().build();
            }
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateJob(String jobName, String jobGroupName, String jobTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 删除任务一个job
     *  
     *
     * @param jobName                 任务名称
     * @param jobGroupName            任务组名
     */
    public void deleteJob(String jobName, String jobGroupName) {
        try {
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 暂停一个job
     *  
     *
     * @param jobName
     * @param jobGroupName
     */
    public void pauseJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 中断一个job
     *  
     *
     * @param jobName
     * @param jobGroupName
     */
    public void interruptJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.interrupt(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 恢复一个job
     *  
     *
     * @param jobName
     * @param jobGroupName
     */
    public void resumeJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 立即执行一个job
     *  
     *
     * @param jobName
     * @param jobGroupName
     */
    public void runAJobNow(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取所有正在运行的job
     *
     * @return
     */
    public List<Map<String, Object>> queryRunJon() {
        List<Map<String, Object>> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                Map<String, Object> map = new HashMap<String, Object>();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("description", "触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime", cronExpression);
                }
                jobList.add(map);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
        return jobList;
    }

    public List<Map<String, String>> getAllJobs() {
        List<Map<String, String>> jobList = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    Trigger trigger = ((List<Trigger>) scheduler.getTriggersOfJob(jobKey)).get(0);
                    Date nextFireTime = trigger.getNextFireTime();
                    JobDetail detail = scheduler.getJobDetail(jobKey);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("jobName", jobName);
                    map.put("jobGroup", jobGroup);
                    map.put("state", getTriggerStatesCN(scheduler.getTriggerState(trigger.getKey()).name()));
                    map.put("clazz", detail.getJobClass().getName());
                    map.put("next", nextFireTime == null ? "" : DateUtil.format(nextFireTime));
                    map.put("cron", getCron(trigger));
                    jobList.add(map);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return jobList;
    }

    private String getCron(Trigger trigger) {
        for (Field f : trigger.getClass().getDeclaredFields()) {
            if (f.getName().equals("cronEx")) {
                try {
                    if (!f.isAccessible())
                        f.setAccessible(true);
                    CronExpression o = (CronExpression) f.get(trigger);
                    return o.getCronExpression();
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return "";
    }

    static Map<String, String> state;

    private static String getTriggerStatesCN(String key) {
        if (state == null) {
            synchronized (QuartzManager.class) {
                if (state == null) {
                    state = new LinkedHashMap<String, String>();
                    state.put("BLOCKED", "阻塞");
                    state.put("COMPLETE", "完成");
                    state.put("ERROR", "出错");
                    state.put("NONE", "不存在");
                    state.put("NORMAL", "正常");
                    state.put("PAUSED", "暂停");

                    state.put("4", "阻塞");
                    state.put("2", "完成");
                    state.put("3", "出错");
                    state.put("-1", "不存在");
                    state.put("0", "正常");
                    state.put("1", "暂停");
                    /*  **STATE_BLOCKED 4 阻塞
                    STATE_COMPLETE 2 完成
                    STATE_ERROR 3 错误
                    STATE_NONE -1 不存在
                    STATE_NORMAL 0 正常
                    STATE_PAUSED 1 暂停***/
                }
            }
        }
        return state.get(key);
    }
}

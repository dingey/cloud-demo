package com.d.web;

import com.d.service.QuartzManager;
import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuartzController {
    private final QuartzManager quartzManager;

    @Autowired
    public QuartzController(QuartzManager quartzManager) {
        this.quartzManager = quartzManager;
    }

    @GetMapping(path = "/quartz/list")
    public Object list() {
        return quartzManager.getAllJobs();
    }

    @PostMapping(path = "/quartz/add")
    public Object add(String clazz, String name, String group, String cron) {
        try {
            Class<?> c = Class.forName(clazz);
            Object o = c.newInstance();
            if (o instanceof Job) {
                Job j = (Job) o;
                boolean b = quartzManager.addJobIfNotExists(j.getClass(), name, group, cron);
                if (b) {
                    return "添加成功";
                } else {
                    return "已存在，添加失败";
                }
            } else {
                return "未实现job接口";
            }
        } catch (ClassNotFoundException e) {
            return "不存在的任务类";
        } catch (IllegalAccessException e) {
            return "任务类无参构造函数不可用";
        } catch (InstantiationException e) {
            return "任务类实例化异常";
        } catch (Exception e) {
            return "表达式不正确";
        }
    }

    @GetMapping(path = "/quartz/pause")
    public Object pause(String name, String group) {
        quartzManager.pauseJob(name, group);
        return "暂停成功";
    }

    @GetMapping(path = "/quartz/resume")
    public Object resume(String name, String group) {
        quartzManager.resumeJob(name, group);
        return "恢复成功";
    }

    @GetMapping(path = "/quartz/del")
    public Object del(String name, String group) {
        quartzManager.deleteJob(name, group);
        return "删除成功";
    }

    @GetMapping(path = "/quartz/edit")
    public Object edit(String name, String group, String cron) {
        quartzManager.updateJob(name, group, cron);
        return "修改成功";
    }

    @GetMapping(path = "/quartz/run")
    public Object run(String name, String group) {
        quartzManager.runAJobNow(name, group);
        return "运行成功";
    }

    @GetMapping(path = "/quartz/delall")
    public Object delall(BatchParam param) {
        check(param);
        for (int i = 0; i < param.name.length; i++) {
            quartzManager.deleteJob(param.name[i], param.group[i]);
        }
        return "删除成功";
    }

    @GetMapping(path = "/quartz/stopall")
    public Object stopall(BatchParam param) {
        check(param);
        for (int i = 0; i < param.name.length; i++) {
            quartzManager.pauseJob(param.name[i], param.group[i]);
        }
        return "暂停成功";
    }

    @GetMapping(path = "/quartz/resumeall")
    public Object resumeall(BatchParam param) {
        check(param);
        for (int i = 0; i < param.name.length; i++) {
            quartzManager.resumeJob(param.name[i], param.group[i]);
        }
        return "恢复成功";
    }

    @GetMapping(path = "/quartz/interruptAll")
    public Object interruptAll(BatchParam param) {
        check(param);
        for (int i = 0; i < param.name.length; i++) {
            quartzManager.interruptJob(param.name[i], param.group[i]);
        }
        return "中断成功";
    }

    @GetMapping(path = "/quartz/interrupt")
    public Object interrupt(String name, String group) {
        quartzManager.interruptJob(name, group);
        return "中断成功";
    }

    private void check(BatchParam param) {
        if (param == null || param.name == null || param.group == null || param.name.length != param.group.length) {
            throw new RuntimeException("请求参数不正确");
        }
    }

    @Getter
    @Setter
    private static class BatchParam {
        private String[] name;
        private String[] group;
    }
}
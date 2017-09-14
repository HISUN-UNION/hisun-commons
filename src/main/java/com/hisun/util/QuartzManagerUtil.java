package com.hisun.util;

import java.util.Date;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>Title: QuartzManagerUtil.java</p>
 * <p>Description: 任务调度管理工具</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 湖南海数互联信息技术有限公司</p>
 *
 * @author Jason
 * @version v0.1
 * @email jason4j@qq.com
 * @date 2015-11-25 20:45
 */
public class QuartzManagerUtil {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private static String TRIGGER_GROUP_NAME = "DEFAULT";

    /**
     * 添加job
     * @param jobName 任务名
     * @param cls     任务
     * @param time    cron表达式
     */
    public static void addJob(String jobName, Class cls, String time) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, cls);// 任务名，任务组，任务执行类
            // 触发器
            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加job
     * @param jobName 任务名
     * @param cls     任务
     * @param time    cron表达式
     */
    public static void addJob(String jobName, Class cls, String time, Map<String,Object> params) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, cls);// 任务名，任务组，任务执行类
            for(Map.Entry<String,Object> entry : params.entrySet()) {
                jobDetail.getJobDataMap().put(entry.getKey(),entry.getValue());
            }
            // 触发器
            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加job
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param time             cron表达式
     */
    public static void addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName, Class jobClass,
                              String time) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
            // 触发器
            CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改job调度时间
     * @param jobName
     * @param time
     */
    public static void modifyJobTime(String jobName, String time,Class clazz,Map<String,Object> map) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            //CronTrigger trigger = (CronTrigger) scheduler.getTrigger(jobName,TRIGGER_GROUP_NAME);
            //if (trigger == null) {
            //    return;
            //}
            //String oldTime = trigger.getCronExpression();
            //if (!oldTime.equalsIgnoreCase(time)) {
            JobDetail jobDetail = scheduler.getJobDetail(jobName,Scheduler.DEFAULT_GROUP);
            if(jobDetail!=null) {
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, clazz,time,map);
//                addJob(jobName, objJobClass, time);
            }else {
                addJob(jobName, clazz,time,map);
            }

            //}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改调度
     * @param triggerName
     * @param triggerGroupName
     * @param time
     */
    public static void modifyJobTime(String triggerName,
                                     String triggerGroupName, String time) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerName,triggerGroupName);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                CronTrigger ct = (CronTrigger) trigger;
                // 修改时间
                ct.setCronExpression(time);
                // 重启触发器
                scheduler.resumeTrigger(triggerName, triggerGroupName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除job
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
            scheduler.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
            scheduler.deleteJob(jobName, Scheduler.DEFAULT_GROUP);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除job
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
            scheduler.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
            scheduler.deleteJob(jobName, jobGroupName);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动所有job
     */
    public static void startJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 停止所有job
     */
    public static void shutdownJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 立即执行一次
     */
    public static void immediate(Class cls, Map<String,Object> params) {
    	try {
    		String jobName=String.valueOf(System.nanoTime());
    		Scheduler scheduler = schedulerFactory.getScheduler();
    		JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, cls);// 任务名，任务组，任务执行类
    		if(params!=null){
    			for(Map.Entry<String,Object> entry : params.entrySet()) {
    				jobDetail.getJobDataMap().put(entry.getKey(),entry.getValue());
    			}
    		}
    		Trigger trigger=new SimpleTrigger(jobName,TRIGGER_GROUP_NAME,0,0);//立即执行一次
    		scheduler.scheduleJob(jobDetail, trigger);
    		// 启动
    		if (!scheduler.isShutdown()) {
    			scheduler.start();
    		}
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    /**
     * 修改job调度时间,加强修正版
     * @param jobName
     * @param time
     */
    public static void modifyJobTime(String jobName,Class clazz, String time,Map<String,Object> map,Date startTime) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(jobName,Scheduler.DEFAULT_GROUP);
            if(jobDetail!=null) {
                removeJob(jobName);
            }
            addJob(jobName, clazz,time,map,startTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 修改job调度时间,加强修正版
     * @param jobName
     * @param time
     */
    public static void modifyJobTime(String jobName,Class clazz, String time,Map<String,Object> map) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(jobName,Scheduler.DEFAULT_GROUP);
            if(jobDetail!=null) {
                removeJob(jobName);
            }
            addJob(jobName, clazz,time,map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 添加job
     * @param jobName 任务名
     * @param cls     任务
     * @param time    cron表达式
     */
    public static void addJob(String jobName, Class cls, String time, Map<String,Object> params,Date startTime) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, cls);// 任务名，任务组，任务执行类
            for(Map.Entry<String,Object> entry : params.entrySet()) {
                jobDetail.getJobDataMap().put(entry.getKey(),entry.getValue());
            }
            // 触发器
            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setStartTime(startTime);
            trigger.setCronExpression(time);// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

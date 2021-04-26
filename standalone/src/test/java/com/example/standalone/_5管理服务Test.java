package com.example.standalone;

import org.flowable.engine.ManagementService;
import org.flowable.engine.impl.jobexecutor.TimerStartEventJobHandler;
import org.flowable.job.api.*;
import org.flowable.job.service.impl.persistence.entity.JobEntity;
import org.flowable.job.service.impl.persistence.entity.JobEntityImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Job Management(作业管理)
 */
public class _5管理服务Test extends _0BaseTests {

    @Test
    public void 工作任务常用操作() {

        //工作查询对象
        JobQuery jobQuery = managementService.createJobQuery();
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
        TimerJobQuery timerJobQuery = managementService.createTimerJobQuery();
        //工作的转移
        //managementService.moveJobToDeadLetterJob("")
        //工作的删除
        //managementService.deleteJob("");
        List<Job> list = jobQuery.list();
        list.forEach(job -> {
            //重试次数，如果超过，自动把JOB放到act_ru_deadletter_job表
            managementService.setJobRetries(job.getId(), 1);
            //手动执行job 会忽略配置异步执行
            managementService.executeJob(job.getId());
        });
    }

    @Test
    public void 手动建立Job() {

        // 时间计算
        Date now = new Date();
        // delay为相较当前时间，延时的时间变量
        Date target = new Date(now.getTime() + 10 * 60 * 1000);
        // 时间事件声明
        JobEntity jobEntity = new JobEntityImpl();
        jobEntity.setDuedate(target);
        jobEntity.setExclusive(true);
        jobEntity.setJobHandlerConfiguration("customProcessKey");// 这里存入需要启动的流程key
        jobEntity.setJobHandlerType(TimerStartEventJobHandler.TYPE);
        // 保存作业事件
//        Context.getCommandContext().getJobEntityManager().insertJobEntity(jobEntity);

    }
}

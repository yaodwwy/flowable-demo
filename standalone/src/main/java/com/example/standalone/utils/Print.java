package com.example.standalone.utils;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Event;
import org.flowable.idm.api.Group;
import org.flowable.job.api.Job;
import org.flowable.task.api.Task;

import java.text.SimpleDateFormat;

@Slf4j
public final class Print {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public static void out(Job job) {
        log.info("================ 工作队列 ==============");
        log.info("{}",JSONUtil.toJsonPrettyStr(job));
    }

    public static void out(Task task) {
        log.info("================ 流程任务 ==============");
        log.info("{}",JSONUtil.toJsonPrettyStr(task));
    }

    public static void out(Event event) {
        log.info("{}",JSONUtil.toJsonPrettyStr(event));
    }

    public static void out(ProcessInstance pi) {
        log.info("=============== 流程实例 ==============");
        String prettyStr = null;
        try {
            prettyStr = JSONUtil.toJsonPrettyStr(pi);
        } catch (Exception e) {
            prettyStr = "TenantId: " + pi.getTenantId();
            log.warn(e.getMessage());
        }
        log.info("{}", prettyStr);
    }

    public static void out(ProcessDefinition pd) {
        log.info("=============== 流程定义 ==============");
        log.info("{}",JSONUtil.toJsonPrettyStr(pd));
    }

    @SneakyThrows
    public static void out(HistoricProcessInstance hpi) {
        log.info("=============== 历史流程实例 ==============");
        log.info("{}",JSONUtil.toJsonPrettyStr(hpi));
    }


    public static void out(Execution execution) {
        log.info("=============== 执行流 ==============");
        log.info("{}",JSONUtil.toJsonPrettyStr(execution));
    }

    @SneakyThrows
    public static void out(Group group) {
        log.info("=============== 用户组 ==============");
        log.info("{}",JSONUtil.toJsonPrettyStr(group));
    }
}

package com.example.standalone.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Event;
import org.flowable.idm.api.Group;
import org.flowable.task.api.Task;

import java.text.SimpleDateFormat;

@Slf4j
public final class Print {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final static ObjectMapper objectMapper = new ObjectMapper();

    public static void out(Task task) {
        log.info("================ 流程任务 ==============");
        log.info("[Task：{}] [name：{}] [owner：{}] [assignee：{}] [executionId：{}] [TaskLocalVariables：{}] " +
                        "[ProcessVariables：{}] [DelegationState：{}]",
                task.getId(), task.getName(), task.getOwner(), task.getAssignee(), task.getExecutionId()
                , task.getTaskLocalVariables(), task.getProcessVariables(), task.getDelegationState());
    }

    public static void out(Event event) {
        log.info("================ 事件 ==============");
        log.info("[Event：{}] [Action：{}] " +
                        "[Message：{}] [TaskId：{}] [UserId：{}] " +
                        "[Time：{}]",
                event.getId(), event.getAction(), event.getMessage(), event.getTaskId(), event.getUserId()
                , event.getTime());
    }

    public static void out(ProcessInstance pi) {
        log.info("=============== 流程实例 ==============");
        log.info("[ProcessInstance：{}] [name：{}] [结束状态：{}] [BusinessKey：{}] [ProcessVariables：{}] [StartTime：{}] " +
                        "[StartUserId：{}] [Description：{}] [ProcessDefinitionName：{}] [ProcessDefinitionKey：{}]",
                pi.getId(), pi.getName(),pi.isEnded(), pi.getBusinessKey(), pi.getProcessVariables(),
                sdf.format(pi.getStartTime()), pi.getStartUserId(), pi.getDescription(),
                pi.getProcessDefinitionName(), pi.getProcessDefinitionKey());
    }

    public static void out(HistoricProcessInstance hpi) {
        log.info("=============== 历史流程实例 ==============");
        log.info("[ProcessInstance：{}]" +
                        " [name：{}] [BusinessKey：{}] " +
                        "[ProcessVariables：{}] " +
                        "[StartTime：{}] " +
                        "[StartUserId：{}] [Description：{}]" +
                        " [ProcessDefinitionName：{}] [ProcessDefinitionKey：{}]",
                hpi.getId(), hpi.getName(), hpi.getBusinessKey(), hpi.getProcessVariables(),
                sdf.format(hpi.getStartTime()), hpi.getStartUserId(), hpi.getDescription(),
                hpi.getProcessDefinitionName(), hpi.getProcessDefinitionKey());
    }


    public static void out(Execution e) {
        log.info("=============== 执行流 ==============");

        log.info("[Execution：{}] [name：{}] [ActivityId：{}] [ParentId：{}] [ProcessInstanceId：{}] " +
                        "[RootProcessInstanceId：{}] [Description：{}] [isEnded：{}]",
                e.getId(), e.getName(), e.getActivityId(), e.getParentId(), e.getProcessInstanceId(),
                e.getRootProcessInstanceId(), e.getDescription(), e.isEnded());
    }

    @SneakyThrows
    public static void out(Group group) {
        log.info("=============== 用户组 ==============");

        log.info("[Group：{}] [name：{}] [Type：{}]",
                group.getId(), group.getName(), group.getType());
    }
}

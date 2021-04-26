package com.example.standalone.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.task.api.Task;

import java.text.SimpleDateFormat;

@Slf4j
public final class Print {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final static ObjectMapper objectMapper = new ObjectMapper();

    public static void out(Task task) {
        log.info("================ 当前流程任务 ==============");
        log.info("[Task：{}] [name：{}] [owner：{}] [assignee：{}] [executionId：{}] [TaskLocalVariables：{}] " +
                        "[ProcessVariables：{}] [DelegationState：{}]",
                task.getId(), task.getName(), task.getOwner(), task.getAssignee(), task.getExecutionId()
                , task.getTaskLocalVariables(), task.getProcessVariables(), task.getDelegationState());
    }

    public static void out(ProcessInstance pi) {
        log.info("=============== 当前流程实例 ==============");
        log.info("[ProcessInstance：{}] [name：{}] [BusinessKey：{}] [ProcessVariables：{}] [StartTime：{}] " +
                        "[StartUserId：{}] [Description：{}] [ProcessDefinitionName：{}] [ProcessDefinitionKey：{}]",
                pi.getId(), pi.getName(), pi.getBusinessKey(), pi.getProcessVariables(),
                sdf.format(pi.getStartTime()), pi.getStartUserId(), pi.getDescription(),
                pi.getProcessDefinitionName(), pi.getProcessDefinitionKey());
    }


    public static void out(Execution e) {
        log.info("=============== 当前执行流节点 ==============");

        log.info("[Execution：{}] [name：{}] [ActivityId：{}] [ParentId：{}] [ProcessInstanceId：{}] " +
                        "[RootProcessInstanceId：{}] [Description：{}] [isEnded：{}]",
                e.getId(), e.getName(), e.getActivityId(), e.getParentId(), e.getProcessInstanceId(),
                e.getRootProcessInstanceId(), e.getDescription(), e.isEnded());
    }

    @SneakyThrows
    public static void out(Group g) {
        log.info("group: {}", objectMapper.writeValueAsString(g));
    }
}

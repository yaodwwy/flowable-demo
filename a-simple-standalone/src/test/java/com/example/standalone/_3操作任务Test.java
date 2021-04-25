package com.example.standalone;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.standalone.utils.Print;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ExecutionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 候选、签收、持有人测试类
 * 用户任务分类：
 * 分为4中状态：未签收/待办理、已签收/办理中、运行中/办理中、已完成/已办结
 */
@Slf4j
public class _3操作任务Test extends _0BaseTests {

    private String random = UUID.randomUUID().toString().substring(0, 3);

    @Test
    public void 任务操作() {
        // 创建任务
        String taskId = "task" + random;
        Task task = taskService.newTask(taskId);
        task.setName("测试任务");
        taskService.saveTask(task);

        // 创建用户
        String userId = "user" + random;
        User user = identityService.newUser(userId);
        user.setFirstName("adam");
        identityService.saveUser(user);

        // 创建组
        String groupID = "group" + random;
        Group group = identityService.newGroup(groupID);
        group.setName("userGroup");
        group.setType("userGroupType");
        identityService.saveGroup(group);

        //创建用户关系
        identityService.createMembership(userId, groupID);

        // 设置任务的候选用户
        taskService.addCandidateUser(taskId, userId);
        // 设置任务的候选用户组
        taskService.addCandidateGroup(taskId, groupID);

        // 待签收/待办理
        log.info(userId + "这个用户有权限处理的任务有： ");
        taskService.createTaskQuery().taskCandidateUser(userId).list().parallelStream().forEach(Print::out);

        // 签收任务
        taskService.claim(taskId, userId);

        //按用户查询指派、签收的任务 用户待办
        log.info(userId + "这个用户有待办的任务有:");
        taskService.createTaskQuery().taskAssignee(userId).list().parallelStream().forEach(Print::out);

        // 签收人是持有人
        taskService.setOwner(taskId, userId);
        // assignee 受理人 是可以由持有人再委托

        //委托
        log.info("委托之前：");
        taskService.createTaskQuery().taskOwner(userId).list().parallelStream().forEach(Print::out);
        taskService.delegateTask(task.getId(), "adam2");
        log.info("委托之后：");
        taskService.createTaskQuery().taskOwner(userId).list().parallelStream().forEach(Print::out);
        //结果：owner是userId，assignee是adam2
    }


    @Test
    public void 任务参数与附件() {
        Task task = taskService.newTask(UUID.randomUUID().toString());
        task.setName("参数测试任务");
        taskService.saveTask(task);
        taskService.setVariable(task.getId(), "var1", "hello");
        Map<String, Object> variables = taskService.getVariables(task.getId());
        log.info("文本参数: " + variables.toString());

        JSONObject obj = JSONUtil.createObj();
        obj.set("id", 1);
        obj.set("name", "adam");
        // 对象参数
        taskService.setVariable(task.getId(), "user", obj);
        JSONObject pr = taskService.getVariable(task.getId(), "user", JSONObject.class);
        log.info("对象参数: {}", pr);
        Assert.notNull(pr, "null ? ");

        //任务Local变量
        ProcessInstance pi = flowable.deployAndStart("processes/_3任务变量.bpmn20.xml", "_3任务变量");
        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //dataObject 流程配置文件变量
        String var = taskService.getVariable(task.getId(), "var", String.class);
        log.info("流程配置文件变量: " + var);

        taskService.setVariableLocal(task.getId(), "days", 3);
        log.info("任务Local变量完成前：" + task.getName() + ", days参数：" + taskService.getVariableLocal(task.getId(), "days"));

        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        log.info("任务Local变量完成后：" + task.getName() + ", days参数：" + taskService.getVariableLocal(task.getId(), "days"));
    }

    @Test
    public void 参数作用域() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_3任务参数作用域.bpmn20.xml", "_3任务参数作用域");
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        tasks.parallelStream().forEach(Print::out);
        for (Task task : tasks) {
            ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
            Execution exe = executionQuery.executionId(task.getExecutionId()).singleResult();
            //在执行流里设置任务参数
            if ("TaskA".equals(task.getName())) {
                runtimeService.setVariableLocal(exe.getId(), "taskVarA", "varA");
            } else {
                runtimeService.setVariable(exe.getId(), "taskVarB", "varB");
            }
        }

        log.info("结束所有任务");
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }

        Task taskC = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Print.out(taskC);
        log.info("taskVarA 的参数：" + runtimeService.getVariable(processInstance.getId(), "taskVarA"));
        log.info("taskVarB 的参数: " + runtimeService.getVariable(processInstance.getId(), "taskVarB"));
        log.info("流程实例: ");
        Print.out(processInstance);
    }
}

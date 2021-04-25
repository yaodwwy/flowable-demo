package com.example.standalone;

import com.example.standalone.delegate.SimpleDelegate;
import com.example.standalone.model.SimpleUser;
import com.example.standalone.service.CandidateUserService;
import com.example.standalone.utils.Print;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 其它任务
 * 手工任务 (抛出)
 * 接收任务 (捕获)RuntimeService.trigger
 * 邮件任务
 * Mule任务
 * 业务规则任务
 */


public class _7BPMN流程任务Test extends _0BaseTests {

    private String random = UUID.randomUUID().toString().substring(0, 3);

    @Test
    public void 用户任务权限() {
        // 创建用户组
        if (identityService.createGroupQuery().groupId("boss").count() == 0) {
            Group bossG = identityService.newGroup("boss");
            bossG.setName("boss");
            identityService.saveGroup(bossG);
        }

        if (identityService.createGroupQuery().groupId("management").count() == 0) {
            Group mangG = identityService.newGroup("management");
            mangG.setName("management");
            identityService.saveGroup(mangG);
        }

        if (identityService.createUserQuery().userId("angus").count() == 0) {
            User user = identityService.newUser("angus");
            user.setFirstName("Angus");
            identityService.saveUser(user);
        }

        flowable.deployAndStart("processes/_7BPMN用户任务权限.bpmn20.xml");

        // 查询各个用户下面有权限看到的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("boss").list();
        System.out.println("boss用户组，可以认领的任务：");
        tasks.parallelStream().forEach(Print::out);

        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        System.out.println("management用户组，可以认领的任务：");
        tasks.parallelStream().forEach(Print::out);

        tasks = taskService.createTaskQuery().taskCandidateUser("angus").list();
        System.out.println("angus用户，可以认领的任务：");
        tasks.parallelStream().forEach(Print::out);

        tasks = taskService.createTaskQuery().taskCandidateUser("adam").list();
        System.out.println("adam用户，可以认领的任务：");
        tasks.parallelStream().forEach(Print::out);

    }

    @Test
    public void JUEL表达式动态代理人() {

        if (identityService.createUserQuery().userId("userA").count() == 0) {
            User userA = identityService.newUser("userA");
            userA.setFirstName("Angus");
            identityService.saveUser(userA);
        }

        if (identityService.createUserQuery().userId("userB").count() == 0) {
            User userB = identityService.newUser("userB");
            userB.setFirstName("Angus");
            identityService.saveUser(userB);
        }

        Deployment dep = repositoryService.createDeployment().addClasspathResource("processes/_7BPMN用户任务JUEL表达式.bpmn20.xml").deploy();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("candidateUserService", new CandidateUserService());
        runtimeService.startProcessInstanceById(pd.getId(), vars);

        // 查询各个用户下面有权限看到的任务

        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("userA").list();
        System.out.println("userA 用户，可以认领的任务：");
        tasks.parallelStream().forEach(Print::out);

        tasks = taskService.createTaskQuery().taskCandidateUser("userB").list();
        System.out.println("userB 用户，可以认领的任务：");
        tasks.parallelStream().forEach(Print::out);
    }

    @Test
    public void 服务任务委托表达式() {
        SimpleDelegate delegate = new SimpleDelegate();
        Map<String, Object> vars = new HashMap<>(1);
        vars.put("delegate", delegate);
        flowable.deployAndStart(vars, "processes/_7BPMN服务任务委托表达式.bpmn20.xml", "服务任务委托表达式");
    }

    /**
     * 流程文件里注入bean在流程文件里定义变量代码里取值
     */
    @Test
    public void 服务任务JUEL表达式赋值取值测试() {
        Map<String, Object> vars = new HashMap<>(1);
        vars.put("bean", new SimpleUser());
        ProcessInstance processInstance = flowable.deployAndStart(vars, "processes/_7BPMN服务任务JUEL表达式.bpmn20.xml", "BPMN服务任务JUEL表达式");
        System.out.println("取到的参数值：" + runtimeService.getVariable(processInstance.getId(), "name"));
    }

    @Test
    public void JavaShell任务() throws IOException {
        //创建命令集合
        List<String> argList = new ArrayList<String>();
        argList.add("cmd");
        argList.add("/c");
        argList.add("echo");
        argList.add("hello");
        argList.add("crazyit");
        ProcessBuilder processBuilder = new ProcessBuilder(argList);
        // 执行命令返回进程
        Process process = processBuilder.start();
        // 解析输出
        String result = IOUtils.toString(process.getInputStream(), Charset.forName("utf-8"));
        System.out.println(result);
    }

    @Test
    public void BPMNShell任务() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_7BPMN服务任务Shell任务.bpmn20.xml");
        System.out.println("返回结果：" + runtimeService.getVariable(processInstance.getId(), "javaHome"));
    }

    @Test
    public void BPMN任务监听器() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_7BPMN任务监听器.bpmn20.xml");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());
    }


}

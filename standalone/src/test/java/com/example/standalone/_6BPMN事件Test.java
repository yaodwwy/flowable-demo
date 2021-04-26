package com.example.standalone;

import com.example.standalone.utils.Print;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;

import java.util.List;


public class _6BPMN事件Test extends _0BaseTests {

    @Test
    public void 定时器开始事件() throws InterruptedException {
        flowable.deployAndStart("processes/_6BPMN定时器开始事件.bpmn20.xml");
        long dataCount = runtimeService.createProcessInstanceQuery().count();
        System.out.println("5秒自动开启一个实例，sleep前流程实例数量：" + dataCount);
        for (int i = 0; i < 10; i++) {
            dataCount = runtimeService.createProcessInstanceQuery().count();
            Thread.sleep(6000);
            System.out.println("sleep后流程实例数量：" + dataCount);
        }
    }

    @Test
    public void 消息开始事件() {
        repositoryService.createDeployment().addClasspathResource("processes/_6BPMN消息开始事件.bpmn20.xml").deploy();
        ProcessInstance pi = runtimeService.startProcessInstanceByMessage("msgName");
        Print.out( pi);
    }
    @Test
    public void 错误开始事件() {
        flowable.deployAndStart("processes/_6BPMN错误开始事件.bpmn20.xml");
    }


    @Test
    public void 中间定时器捕获事件() throws Exception {
        ProcessInstance pi = flowable.deployAndStart("processes/_6BPMN中间定时器捕获事件.bpmn20.xml");
        flowable.complete(pi);
        Thread.sleep(10_000);
        flowable.complete(pi);
    }

    @Test
    public void 中间信号捕获事件() {
        ProcessInstance pi = flowable.deployAndStart("processes/_6BPMN中间信号捕获事件.bpmn20.xml");
        flowable.complete(pi);
    }


    @Test
    public void 中间抛出事件补偿() {
        flowable.deployAndStart("processes/_6BPMN中间抛出事件补偿.bpmn20.xml");
    }

    @Test
    public void 边界定时事件() throws InterruptedException {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_6BPMN边界定时事件.bpmn20.xml");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());
        System.out.println("等10秒后会自动超时...");
        Thread.sleep(1000 * 10);
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());
    }

    @Test
    public void 边界信号事件() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_6BPMN边界信号事件.bpmn20.xml");
        
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());
        // 完成第一个任务
        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());

        // 信号触发
        System.out.println("信号触发->>contactChangeSignal");
        runtimeService.signalEventReceived("contactChangeSignal");

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前任务：" + task.getName());
    }

    @Test
    public void 错误结束事件() {
        flowable.deployAndStart("processes/_6BPMN错误结束事件.bpmn20.xml");
        System.out.println("错误结束事件：结束时触发...");
    }

    @Test
    public void 取消结束事件() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_6BPMN取消结束事件.bpmn20.xml");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前流程任务：");
        Print.out(task);
        taskService.complete(task.getId());
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("当前流程任务：");
        Print.out(task);
    }


    @Test
    public void 终止结束事件() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_6BPMN终止结束事件.bpmn20.xml");
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("终结前执行流：");
        list.parallelStream().forEach(Print::out);

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task task : tasks) {
            if (task.getName().equals("Task1")) {
                taskService.complete(task.getId());
            }
        }

        list = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("终结后执行流：");

        list.parallelStream().forEach(Print::out);

        ProcessInstance newPi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("流程实例已终结，Task3不可达！");
    }
}

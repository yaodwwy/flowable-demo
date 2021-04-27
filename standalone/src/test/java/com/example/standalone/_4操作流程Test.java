package com.example.standalone;

import com.example.domain.delegate.ExceptionThrowDelegate;
import com.example.domain.delegate.SimpleDelegate;
import com.example.standalone.utils.Print;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Event;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 工作的产生与管理
 * 异步任务
 * 定时事件
 * 暂停的工作
 * 无法执行的工作
 */
@Slf4j
public class _4操作流程Test extends _0BaseTests {

    @Test
    public void 暂停挂起流程定义() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_4暂停挂起流程定义.bpmn20.xml");
        String processDefinitionID = processInstance.getProcessDefinitionId();
        repositoryService.suspendProcessDefinitionById(processDefinitionID);
        try {
            runtimeService.startProcessInstanceById(processDefinitionID);
        } catch (FlowableException e) {
            String message = e.getMessage();
            log.info(message);
            boolean suspended = message.endsWith("suspended");
            Assert.isTrue(suspended, "Error occurred");
        }
    }

    @Test
    public void 捕获信号事件触发流程继续() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_4捕获信号事件触发流程继续.bpmn20.xml");

        // 查当前的子执行流（只有一个）
        List<Execution> exe = flowable.listChildExecution(processInstance.getId());
        exe.parallelStream().forEach(Print::out);
        flowable.signalEventReceived("testSignal");
        //再查一次
        exe = flowable.listChildExecution(processInstance.getId());
        exe.parallelStream().forEach(Print::out);
    }

    @Test
    public void 捕获消息事件触发流程继续() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_4捕获消息事件触发流程继续.bpmn20.xml");
//        MessageEvent.bpmn
        // 查当前的子执行流（只有一个）
        List<Execution> exe = flowable.listChildExecution(processInstance.getId());
        exe.parallelStream().forEach(Print::out);

        // 一个消息触发的中间捕获事件
        // 让它往前走
        runtimeService.messageEventReceived("testMsg", exe.get(0).getId());
        flowable.listChildExecution(processInstance.getId()).parallelStream().forEach(Print::out);
    }

    @Test
    public void 接收任务触发流程继续() {
        ProcessInstance processInstance = flowable.deployAndStart("processes/_4接收任务触发流程继续.bpmn20.xml");
        // 查当前的子执行流（只有一个）
        List<Execution> exe = flowable.listChildExecution(processInstance.getId());
        exe.parallelStream().forEach(Print::out);
        // 等待任务，也就是说需要手动推进下一步的执行
        // 让它往前走
        flowable.trigger(exe.get(0).getId());
        flowable.listChildExecution(processInstance.getId()).parallelStream().forEach(Print::out);
    }

    @Test
    public void 流程挂起() throws InterruptedException {
        if (!flowable.isAsyncExecutorActivate()) {
            log.error("未打开异步，不能测试！");
            return;
        }
        ProcessInstance processInstance = flowable.deployAndStart("processes/_4流程操作.bpmn20.xml");
        String processInstanceID = processInstance.getId();
        Print.out(processInstance);
        log.info("==================等5秒以上再关！等下会有异常处理类打印==================");

        /*
        ISO_8601格式：（P ,Y,M,W,D,T,.H,M,S）
        当一个流程被挂起后,是不能继续新建立这个流程的实例了,会有异常抛出,请注意在上面的方法中,
        可以设定这个流程实例的过期时间,也可以通过流程实例id去挂起激活流程:
         */
        // 中止
        flowable.suspendProcessInstanceById(processInstanceID);
        Thread.sleep(18_000);
        // 再激活
        flowable.activateProcessInstanceById(processInstanceID);
    }


    @SneakyThrows
    @Test
    public void 流程异常抛出() {

        ExceptionThrowDelegate exceptionThrowDelegate = new ExceptionThrowDelegate();

        ProcessInstance pi = flowable.deployAndStart(
                Map.of("exceptionThrowDelegate", exceptionThrowDelegate),
                "processes/_4流程异常处理.bpmn20.xml", "流程异常处理");
        String processInstanceID = pi.getId();
        flowable.complete(pi);
        List<Event> events = runtimeService.getProcessInstanceEvents(pi.getId());
        events.parallelStream().forEach(Print::out);
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId(pi.getId()).list();
        list.parallelStream().forEach(Print::out);

    }

}

package com.example.standalone;

import com.example.standalone.utils.Print;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 工作的产生与管理
 * 异步任务
 * 定时事件
 * 暂停的工作
 * 无法执行的工作
 */
@Slf4j
public class _4流程操作Test extends _0BaseTests {

    @Test
    public void 暂停挂起流程定义() {
        ProcessInstance processInstance = factory.deployAndStart("processes/_4暂停挂起流程定义.bpmn20.xml");
        String processDefinitionID = processInstance.getProcessDefinitionId();
        repositoryService.suspendProcessDefinitionById(processDefinitionID);
        try {
            runtimeService.startProcessInstanceById(processDefinitionID);
        } catch (FlowableException e) {
            String message = e.getMessage();
            log.info(message);
            boolean suspended = message.endsWith("suspended");
            Assert.isTrue(suspended,"Error?");
        }
    }

    @Test
    public void 捕获信号事件触发流程继续() {
        ProcessInstance processInstance = factory.deployAndStart("processes/_4捕获信号事件触发流程继续.bpmn20.xml");

        // 查当前的子执行流（只有一个）
        List<Execution> exe = factory.listChildExecution(processInstance.getId());
        Print.exec(exe);
        factory.signalEventReceived("testSignal");
        //再查一次
        exe = factory.listChildExecution(processInstance.getId());
        Print.exec(exe);
    }

    @Test
    public void 捕获消息事件触发流程继续() {
        ProcessInstance processInstance = factory.deployAndStart("processes/_4捕获消息事件触发流程继续.bpmn20.xml");
//        MessageEvent.bpmn
        // 查当前的子执行流（只有一个）
        List<Execution> exe = factory.listChildExecution(processInstance.getId());
        Print.exec(exe);

        // 一个消息触发的中间捕获事件
        // 让它往前走
        runtimeService.messageEventReceived("testMsg", exe.get(0).getId());
        exe = factory.listChildExecution(processInstance.getId());
        Print.exec(exe);
    }

    @Test
    public void 接收任务触发流程继续() {
        ProcessInstance processInstance = factory.deployAndStart("processes/_4接收任务触发流程继续.bpmn20.xml");
        // 查当前的子执行流（只有一个）
        List<Execution> exe = factory.listChildExecution(processInstance.getId());
        Print.exec(exe);
        // 等待任务，也就是说需要手动推进下一步的执行
        // 让它往前走
        factory.trigger(exe.get(0).getId());
        exe = factory.listChildExecution(processInstance.getId());
        Print.exec(exe);
    }

    @Test
    public void 流程操作() throws InterruptedException {
        if (!factory.isAsyncExecutorActivate()) {
            log.error("未打开异步，不能测试！");
            return;
        }
        ProcessInstance processInstance = factory.deployAndStart("processes/_4流程操作.bpmn20.xml");
        String processInstanceID = processInstance.getId();
        Print.instances(processInstance);
        log.info("==================等15秒以上再关！等下会有异常处理类打印==================");

        /*
        ISO_8601格式：（P ,Y,M,W,D,T,.H,M,S）
        当一个流程被挂起后,是不能继续新建立这个流程的实例了,会有异常抛出,请注意在上面的方法中,
        可以设定这个流程实例的过期时间,也可以通过流程实例id去挂起激活流程:
         */
        // 中止
        factory.suspendProcessInstanceById(processInstanceID);
        Thread.sleep(18_000);
        // 再激活
        factory.activateProcessInstanceById(processInstanceID);
    }

}

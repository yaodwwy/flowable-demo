package com.example.standalone;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 流程文件部署
 * addClasspathResource
 * addInputStream
 * addString
 * addZipInputStream
 * addBpmnModel
 * addBytes
 * deploy
 * <p>
 * 流程定义的删除行为：
 * 1. 不管是否指定级联，都会删除部署相关的身份数据、流程定义数据、流程资源与部署数据。
 * 2. 如果设置为级联删除，则会将运行的流程实例、流程任务以及流程实例的历史数据删除。
 * 3. 如果不级联删除，但是存在运行时数据，例如还有流程实例，就会删除失败。
 */
@Slf4j
public class _1部署Test extends _0BaseTests{

    @Test
    public void 部署流程文件() throws IOException {
        ProcessDefinition deploy = flowable.deploy("processes/_1部署流程.bpmn20.xml", "_1部署流程");
        Assert.notNull(deploy, "null?");
        log.info("version: {}", deploy.getVersion());
    }


    @Test
    public void 动态部署() throws Exception {
        // 1. 创建一个空的BpmnModel和Process对象
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        model.addProcess(process);
        process.setId("my-process");

        // 创建Flow元素（所有的事件、任务都被认为是Flow）
        process.addFlowElement(createStartEvent());
        process.addFlowElement(createUserTask("task1", "First task", "fred"));
        process.addFlowElement(createUserTask("task2", "Second task", "john"));
        process.addFlowElement(createEndEvent());

        process.addFlowElement(createSequenceFlow("start", "task1"));
        process.addFlowElement(createSequenceFlow("task1", "task2"));
        process.addFlowElement(createSequenceFlow("task2", "end"));

        // 2. 流程图自动布局（位于activiti-bpmn-layout模块）
        new BpmnAutoLayout(model).execute();

        // 3. 把BpmnModel对象部署到引擎
        Deployment deploy = flowable.buildBpmn("dynamic-model", model, "my-process", true);
        log.info(deploy.getId() + ", " + deploy.getName());

    }

    // 创建用户任务
    protected UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    // 添加流程顺序
    protected SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    // 创建开始事件
    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    // 创建结束事件
    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }
}

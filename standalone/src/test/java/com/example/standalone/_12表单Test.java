package com.example.standalone;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class _12表单Test extends _0BaseTests {

    @Test
    public void 内置表单() {

        ProcessDefinition processDefinition = flowable.deploy("processes/_12内置表单.bpmn20.xml", "_12内置表单");
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        Assert.notNull(startFormData.getFormKey(), "Error!");

        Map<String, String> formProperties = new HashMap<String, String>();
        formProperties.put("name", "HenryYan");
        /**
         * 流程实例是通过提交表单来启动
         */
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), formProperties);
        Assert.notNull(processInstance);

        // 运行时变量
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        assertThat(variables).isNotEmpty().size().isEqualTo(1);
        Set<Map.Entry<String, Object>> entrySet = variables.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        // 历史记录
        List<HistoricDetail> list = historyService.createHistoricDetailQuery().formProperties().processInstanceId(processInstance.getId()).list();
        assertThat(list).isNotEmpty().size().isEqualTo(1);

        // 获取第一个任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.notNull("First Step", task.getName());

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        assertNotNull(taskFormData);
        assertNull(taskFormData.getFormKey());
        List<FormProperty> taskFormProperties = taskFormData.getFormProperties();
        assertNotNull(taskFormProperties);
        for (FormProperty formProperty : taskFormProperties) {
            System.out.println(ToStringBuilder.reflectionToString(formProperty));
        }
        formProperties = new HashMap<String, String>();
        formProperties.put("setInFirstStep", "01/12/2012");
        /**
         * 提交表单并完成任务
         */
        formService.submitTaskFormData(task.getId(), formProperties);

        // 获取第二个节点
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskName("Second Step").singleResult();
        assertNotNull(task);
        taskFormData = formService.getTaskFormData(task.getId());
        assertNotNull(taskFormData);
        List<FormProperty> formProperties2 = taskFormData.getFormProperties();
        for (FormProperty formProperty : formProperties2) {
            System.out.println(formProperty.getName() + " = " + formProperty.getValue());
        }
        assertNotNull(formProperties2);
        assertEquals(2, formProperties2.size());
        assertNotNull(formProperties2.get(0).getValue());
        assertEquals(formProperties2.get(0).getValue(), "01/12/2012");

        variables.put("age", "15");
        taskService.complete(task.getId(), variables);

        long count = taskService.createTaskQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(count, 0);
    }

    @Test
    @Deployment(resources = {"processes/_12外置表单.bpmn20.xml"})
    public void 外置表单() {
        // Get start form
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("FormKey").latestVersion().singleResult();
        String procDefId = processDefinition.getId();
        Object startForm = formService.getRenderedStartForm(procDefId);
        assertNotNull(startForm);
        System.out.println("表单内容：" + startForm);
        assertEquals("<input id=\"start-name\" />", startForm);

        Map<String, String> formProperties = new HashMap<String, String>();
        formProperties.put("startName", "HenryYan");
        System.out.println("提交【开始】表单属性..." + formProperties);
        ProcessInstance processInstance = formService.submitStartFormData(procDefId, formProperties);
        assertNotNull(processInstance);

        Task task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
        Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
        System.out.println("获取表单内容：" + renderedTaskForm);
        assertEquals("<input id=\"start-name\" value=\"HenryYan\" />\n<input id=\"first-name\" />", renderedTaskForm);

        formProperties = new HashMap<String, String>();
        formProperties.put("firstName", "kafeitu");
        System.out.println("提交【完成任务】表单属性..." + formProperties);
        formService.submitTaskFormData(task.getId(), formProperties);

        task = taskService.createTaskQuery().taskAssignee("user2").orderByTaskCreateTime().desc().list().get(0);
        assertNotNull(task);
        renderedTaskForm = formService.getRenderedTaskForm(task.getId());
        System.out.println("获取表单内容：" + renderedTaskForm);
        assertEquals("<input id=\"first-name\" value=\"kafeitu\" />", renderedTaskForm);
    }
}

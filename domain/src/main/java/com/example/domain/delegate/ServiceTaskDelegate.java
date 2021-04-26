package com.example.domain.delegate;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class ServiceTaskDelegate implements JavaDelegate, Serializable {

    private static final long serialVersionUID = 6211732533518352006L;

    @Override
    public void execute(DelegateExecution execution) {

        String className = this.getClass().getName();
        log.info("Class Name: {}", className);

        log.info("活动id: {}", execution.getCurrentActivityId());
        log.info("变量: {}", execution.getVariables());
        log.info("EventName: {}", execution.getEventName());
        log.info("ProcessDefinitionId: {}", execution.getProcessDefinitionId());
        log.info("SuperExecutionId: {}", execution.getSuperExecutionId());
        log.info("Variables: {}", execution.getVariables());

        execution.setVariable(className + " execute", execution.toString());
    }
}

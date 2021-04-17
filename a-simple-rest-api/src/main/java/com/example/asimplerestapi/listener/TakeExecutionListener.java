package com.example.asimplerestapi.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Can be used too:
 * org.flowable.engine.delegate.ExecutionListener
 */
@Slf4j
@Component
public class TakeExecutionListener implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String className = this.getClass().getName();
        log.info("Class Name: {}", className);
        log.info("EventName: {}", execution.getEventName());
        log.info("ProcessDefinitionId: {}", execution.getProcessDefinitionId());
        log.info("SuperExecutionId: {}", execution.getSuperExecutionId());
        log.info("Variables: {}", execution.getVariables());

        execution.setVariable(className + " notify", execution.toString());
    }
}

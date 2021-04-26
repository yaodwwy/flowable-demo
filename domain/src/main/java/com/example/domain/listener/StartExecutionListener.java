package com.example.domain.listener;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * Can be used too:
 * org.flowable.engine.delegate.JavaDelegate
 */
@Slf4j
public class StartExecutionListener implements ExecutionListener {

    private static final long serialVersionUID = -5785907107602739369L;

    @SneakyThrows
    @Override
    public void notify(DelegateExecution execution) {

        String className = this.getClass().getName();
        log.info("Class Name: {}", className);

        log.info("EventName: {}", execution.getEventName());
        log.info("ProcessDefinitionId: {}", execution.getProcessDefinitionId());
        log.info("SuperExecutionId: {}", execution.getSuperExecutionId());
        log.info("Variables: {}", execution.getVariables());

        execution.setVariable(className + " notify", execution.toString());
    }
}

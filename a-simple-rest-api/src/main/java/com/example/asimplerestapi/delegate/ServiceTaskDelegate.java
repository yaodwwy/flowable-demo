package com.example.asimplerestapi.delegate;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class ServiceTaskDelegate implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("活动id: {}", execution.getCurrentActivityId());
        log.info("变量: {}", execution.getVariables());
    }
}

package com.example.asimplerestapi.expression;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.async.AsyncTaskInvoker;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.FutureJavaDelegate;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * SprintBean
 */
@Slf4j
@Component
public class FlowableDelegateExpression implements FutureJavaDelegate {

    @Override
    public CompletableFuture execute(DelegateExecution execution, AsyncTaskInvoker taskInvoker) {
        return null;
    }

    @Override
    public void afterExecution(DelegateExecution execution, Object executionData) {
        log.info("x");
    }

}

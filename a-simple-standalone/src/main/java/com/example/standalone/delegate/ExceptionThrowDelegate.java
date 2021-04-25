package com.example.standalone.delegate;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ExceptionThrowDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.error("EventName: {} \n" +
                        "ProcessDefinitionId: {} \n" +
                        "ProcessInstanceId: {} \n" +
                        "ProcessInstanceBusinessKey: {} \n",
                execution.getEventName()
                , execution.getProcessDefinitionId()
                , execution.getProcessInstanceId()
                , execution.getProcessInstanceBusinessKey());

        Map<String, Object> variables = execution.getVariables();
        log.warn("Variables: >>>>>> \n {}",
                JSONUtil.toJsonStr(variables));

        throw new FlowableException(">>> The Exception throw by JavaDelegate !!!");
    }
}

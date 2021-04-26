package com.example.domain.delegate;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Slf4j
@Component
public class SimpleDelegate implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution execution) {
        log.warn("SimpleDelegate: >>>>>> \n" +
                        "EventName: {} \n" +
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
    }
}

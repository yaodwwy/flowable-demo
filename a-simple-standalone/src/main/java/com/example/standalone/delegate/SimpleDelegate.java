package com.example.standalone.delegate;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SimpleDelegate implements JavaDelegate {

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

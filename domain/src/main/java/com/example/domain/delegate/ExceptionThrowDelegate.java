package com.example.domain.delegate;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Slf4j
@Component
public class ExceptionThrowDelegate implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution execution) {
        log.warn("异常抛出代理类：" +
                        ">>>>>> \n" +
                        "ProcessDefinition: {} \n" +
                        "ProcessInstanceId: {} \n" +
                        "ProcessInstanceBusinessKey: {} \n" +
                        "ProcessInstanceBusinessKey: {} \n",
                execution.getProcessDefinitionId()
                , execution.getProcessInstanceId()
                , execution.getProcessInstanceBusinessKey()
                , execution.getRootProcessInstanceId());

        Map<String, Object> variables = execution.getVariables();
        log.warn("Variables: >>>>>> \n {}",
                JSONUtil.toJsonStr(variables));
        if ("exception".equals(execution.getVariable("myVariable"))) {
            throw new FlowableException(">>> The Exception throw by JavaDelegate !!!");
        }
    }
}

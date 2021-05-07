package com.example.standalone;

import com.example.standalone.utils.Print;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

@Slf4j
public class _13租户Test extends _0BaseTests {

    @Test
    void 租户失败降级() {

        ProcessDefinition deploy = flowable.deploy("processes/_13租户降级.bpmn20.xml", "_13租户降级");
        Print.out(deploy);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("_13租户降级key", "AAA");
        log.info("租户：" + processInstance.getTenantId() + " 启动成功！");
    }
}

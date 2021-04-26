package com.example.standalone;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Data
public class _0BaseTests {

    FlowableUtils flowable;
    IdentityService identityService;
    RuntimeService runtimeService;
    TaskService taskService;
    FormService formService;
    RepositoryService repositoryService;
    ManagementService managementService;
    HistoryService historyService;

    public _0BaseTests() {
        ProcessEngine processEngine = FlowableStandaloneConfig.processEngine();
        this.identityService = processEngine.getIdentityService();
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.formService = processEngine.getFormService();
        this.formService = processEngine.getFormService();
        this.repositoryService = processEngine.getRepositoryService();
        this.managementService = processEngine.getManagementService();
        this.flowable = new FlowableUtils(processEngine, repositoryService, runtimeService, taskService, managementService);
    }

    @Test
    void initTest() {
        log.info("init");
    }
}

package com.example.standalone;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.junit.jupiter.api.Test;

@Slf4j
@Data
public class _0BaseTests {

    FlowableUtils flowable;
    ProcessEngineConfiguration processEngineConfiguration;
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
        this.historyService = processEngine.getHistoryService();
        this.repositoryService = processEngine.getRepositoryService();
        this.managementService = processEngine.getManagementService();
        this.processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        this.flowable = new FlowableUtils(processEngine, repositoryService, runtimeService, taskService, managementService);
    }

    @Test
    void initTest() {
        log.info("init");
    }
}

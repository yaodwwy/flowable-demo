package com.example.standalone;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class _0BaseTests {

    @Autowired
    FlowableFactory factory;

    @Autowired
    IdentityService identityService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    FlowableFactory ActivitiFactory;

    @Autowired
    TaskService taskService;


    @Autowired
    RepositoryService repositoryService;

    @Test
    void contextLoads() {
    }

}

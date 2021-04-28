package com.example.standalone;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.junit.jupiter.api.Test;

import static com.example.standalone.FlowableStandaloneConfig.*;

@Slf4j
public class _0ASimpleTest {

    @Test
    void firstTest() {

        ProcessEngine processEngine = FlowableStandaloneConfig.processEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("processes/_3任务变量.bpmn20.xml");

        Deployment deploy = deployment.deploy();
        log.info("{}", JSONUtil.toJsonPrettyStr(deploy));
    }
}

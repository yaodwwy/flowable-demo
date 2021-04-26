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

@Slf4j
public class _0ASimpleTest {

    private final static String DATASOURCE_URL = "jdbc:h2:~/flowable-db/db;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9091;DB_CLOSE_DELAY=-1";
    private final static String DATASOURCE_USERNAME = "flowable";
    private final static String DATASOURCE_PASSWORD = "flowable";
    private final static String DATASOURCE_DRIVER = "org.h2.Driver";

    @Test
    void firstTest() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DATASOURCE_URL)
                .setJdbcUsername(DATASOURCE_USERNAME)
                .setJdbcPassword(DATASOURCE_PASSWORD)
                .setJdbcDriver(DATASOURCE_DRIVER)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setAsyncExecutorActivate(true)
                .setHistory(HistoryLevel.AUDIT.getKey())
                .setCreateDiagramOnDeploy(true);

        ProcessEngine processEngine = cfg.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("processes/_3任务变量.bpmn20.xml");

        Deployment deploy = deployment.deploy();
        log.info("{}", JSONUtil.toJsonPrettyStr(deploy));
    }
}
